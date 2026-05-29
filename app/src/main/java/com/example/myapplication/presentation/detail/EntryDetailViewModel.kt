package com.example.myapplication.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Entry
import com.example.myapplication.domain.usecase.GetEntryByIdUseCase
import com.example.myapplication.domain.usecase.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EntryDetailState(
    val entry: Entry? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class EntryDetailViewModel @Inject constructor(
    private val getEntryById: GetEntryByIdUseCase,
    private val toggleBookmark: ToggleBookmarkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EntryDetailState())
    val state: StateFlow<EntryDetailState> = _state

    fun loadEntry(id: String) {
        viewModelScope.launch {
            _state.value = EntryDetailState(isLoading = true)
            val entry = getEntryById(id)
            _state.value = EntryDetailState(entry = entry, isLoading = false)
        }
    }

    fun toggleBookmark(entryId: String) {
        viewModelScope.launch {
            toggleBookmark.invoke(entryId)
            val updated = _state.value.entry?.copy(
                isBookmarked = !(_state.value.entry?.isBookmarked ?: false)
            )
            _state.value = _state.value.copy(entry = updated)
        }
    }
}
