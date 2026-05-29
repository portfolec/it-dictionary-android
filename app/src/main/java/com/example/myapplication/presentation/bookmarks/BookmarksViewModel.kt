package com.example.myapplication.presentation.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Entry
import com.example.myapplication.domain.usecase.GetBookmarkedEntriesUseCase
import com.example.myapplication.domain.usecase.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookmarksState(
    val entries: List<Entry> = emptyList(),
    val selectedFolder: String = "Все",
    val isLoading: Boolean = true
)

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val getBookmarkedEntries: GetBookmarkedEntriesUseCase,
    private val toggleBookmark: ToggleBookmarkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BookmarksState())
    val state: StateFlow<BookmarksState> = _state

    init {
        loadBookmarks()
    }

    private fun loadBookmarks() {
        viewModelScope.launch {
            getBookmarkedEntries().collect { entries ->
                _state.value = _state.value.copy(entries = entries, isLoading = false)
            }
        }
    }

    fun removeBookmark(entryId: String) {
        viewModelScope.launch {
            toggleBookmark(entryId)
        }
    }

    fun selectFolder(folder: String) {
        _state.value = _state.value.copy(selectedFolder = folder)
    }
}
