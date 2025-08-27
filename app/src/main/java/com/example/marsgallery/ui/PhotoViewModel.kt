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

enum class RoverTab(val rover: String, val label: String) {
    Curiosity("curiosity", "Curiosity"),
    Opportunity("opportunity", "Opportunity"),
    Perseverance("perseverance", "Perseverance")
}

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
    private val repository: PhotoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    init {
        refresh() // initial load
    }

    fun selectRover(tab: RoverTab) {
        _state.value = UiState(selectedRover = tab) // reset to defaults for new tab
        refresh()
    }

    fun refresh() {
        LogD("Inside refresh")
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true, page = 1, error = null)
            runCatching {
                repository.getMarsPhotos(
                    rover = _state.value.selectedRover.rover,
                    page = 1
                )
            }.onSuccess { res ->
                LogD("Inside onSuccess")
                _state.value = _state.value.copy(
                    photos = res.photos,
                    isRefreshing = false,
                    page = if (res.photos.isNotEmpty()) 2 else 1
                )
            }.onFailure { e ->
                LogE("Inside onFailure : ${e.message}")
                _state.value = _state.value.copy(isRefreshing = false, error = e.message)
            }
        }
    }

    fun loadMore() {
        // Prevent duplicate loads
        val s = _state.value
        if (s.isRefreshing || s.isLoadingMore) return

        viewModelScope.launch {
            _state.value = s.copy(isLoadingMore = true, error = null)
            runCatching {
                repository.getMarsPhotos(
                    rover = s.selectedRover.rover,
                    page = s.page
                )
            }.onSuccess { res ->
                val newList = s.photos + res.photos
                _state.value = s.copy(
                    photos = newList,
                    isLoadingMore = false,
                    page = if (res.photos.isNotEmpty()) s.page + 1 else s.page
                )
            }.onFailure { e ->
                _state.value = s.copy(isLoadingMore = false, error = e.message)
            }
        }
    }
}
