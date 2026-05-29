package com.example.myapplication.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Entry
import com.example.myapplication.domain.usecase.GetEntriesByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryState(
    val entries: List<Entry> = emptyList(),
    val isLoading: Boolean = true,
    val sortBy: String = "Алфавит"
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getEntriesByCategory: GetEntriesByCategoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CategoryState())
    val state: StateFlow<CategoryState> = _state

    fun loadCategory(categoryName: String) {
        viewModelScope.launch {
            getEntriesByCategory(categoryName).collect { entries ->
                val sorted = when (_state.value.sortBy) {
                    "Алфавит" -> entries.sortedBy { it.title }
                    "Просмотры" -> entries.sortedByDescending { it.views }
                    else -> entries
                }
                _state.value = _state.value.copy(entries = sorted, isLoading = false)
            }
        }
    }

    fun setSortBy(sort: String) {
        _state.value = _state.value.copy(sortBy = sort)
    }
}
