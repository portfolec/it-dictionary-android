package com.example.myapplication.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Entry
import com.example.myapplication.domain.usecase.SearchEntriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchState(
    val query: String = "",
    val results: List<Entry> = emptyList(),
    val isLoading: Boolean = false,
    val selectedCategory: String = "Все"
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchEntries: SearchEntriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state

    fun onQueryChange(query: String) {
        _state.value = _state.value.copy(query = query, isLoading = query.isNotEmpty())
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                searchEntries(query).collect { results ->
                    val filtered = if (_state.value.selectedCategory == "Все") {
                        results
                    } else {
                        results.filter { it.category == _state.value.selectedCategory }
                    }
                    _state.value = _state.value.copy(results = filtered, isLoading = false)
                }
            }
        } else {
            _state.value = _state.value.copy(results = emptyList(), isLoading = false)
        }
    }

    fun onCategorySelect(category: String) {
        _state.value = _state.value.copy(selectedCategory = category)
        val query = _state.value.query
        if (query.isNotEmpty()) onQueryChange(query)
    }
}
