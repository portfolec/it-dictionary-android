package com.example.myapplication.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Entry
import com.example.myapplication.domain.repository.EntryRepository
import com.example.myapplication.domain.usecase.GetPopularEntriesUseCase
import com.example.myapplication.domain.usecase.GetRecentEntriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val popularEntries: List<Entry> = emptyList(),
    val recentEntries: List<Entry> = emptyList(),
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularEntries: GetPopularEntriesUseCase,
    private val getRecentEntries: GetRecentEntriesUseCase,
    private val repository: EntryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        syncAndLoad()
    }

    private fun syncAndLoad() {
        viewModelScope.launch {
            // Сначала синхронизируем с сервером
            _state.value = _state.value.copy(isSyncing = true)
            try {
                repository.syncWithRemote()
            } catch (e: Exception) {
                // Если сервер недоступен — работаем с локальными данными
            }
            _state.value = _state.value.copy(isSyncing = false)

            // Загружаем из Room (уже обновлённый)
            combine(
                getPopularEntries(),
                getRecentEntries()
            ) { popular, recent ->
                HomeState(
                    popularEntries = popular,
                    recentEntries = recent,
                    isLoading = false
                )
            }.collect { state ->
                _state.value = state
            }
        }
    }

    fun refresh() {
        syncAndLoad()
    }
}
