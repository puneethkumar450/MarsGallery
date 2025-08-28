package com.example.marsgallery.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsgallery.Constants.LogD
import com.example.marsgallery.Constants.LogE
import com.example.marsgallery.data.MarsPhoto
import com.example.marsgallery.data.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiState(
    val photos: List<MarsPhoto> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val page: Int = 1, //25 items per page returned
    val selectedRover: RoverTab = RoverTab.Curiosity,
    val error: String? = null,
)

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val repository: PhotoRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        refresh() // initial load
    }

    fun selectRover(tab: RoverTab) {
        _uiState.value = UiState(selectedRover = tab) // reset to defaults for new tab
        refresh()
    }

    fun refresh() {
        LogD("Inside refresh")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, page = 1, error = null)
            runCatching {
                repository.getMarsPhotos(
                    rover = _uiState.value.selectedRover.rover,
                    page = 1
                )
            }.onSuccess { res ->
                LogD("Inside onSuccess")
                _uiState.value = _uiState.value.copy(
                    photos = res.photos,
                    isRefreshing = false,
                    page = if (res.photos.isNotEmpty()) 2 else 1
                )
            }.onFailure { e ->
                LogE("Inside onFailure : ${e.message}")
                _uiState.value = _uiState.value.copy(isRefreshing = false, error = e.message)
            }
        }
    }

    fun loadMoreData() {
        // Prevent duplicate loads
        val s = _uiState.value
        if (s.isRefreshing || s.isLoadingMore) return

        viewModelScope.launch {
            _uiState.value = s.copy(isLoadingMore = true, error = null)
            runCatching {
                repository.getMarsPhotos(
                    rover = s.selectedRover.rover,
                    page = s.page
                )
            }.onSuccess { res ->
                val newList = s.photos + res.photos
                _uiState.value = s.copy(
                    photos = newList,
                    isLoadingMore = false,
                    page = if (res.photos.isNotEmpty()) s.page + 1 else s.page
                )
            }.onFailure { e ->
                _uiState.value = s.copy(isLoadingMore = false, error = e.message)
            }
        }
    }
}


enum class RoverTab(val rover: String, val label: String) {
    Curiosity("curiosity", "Curiosity"),
    Opportunity("opportunity", "Opportunity"),
    Perseverance("perseverance", "Perseverance")
}