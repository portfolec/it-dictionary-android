package com.example.myapplication.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.AdminStats
import com.example.myapplication.domain.model.Entry
import com.example.myapplication.domain.usecase.DeleteEntryUseCase
import com.example.myapplication.domain.usecase.GetAllEntriesUseCase
import com.example.myapplication.domain.usecase.GetEntryByIdUseCase
import com.example.myapplication.domain.usecase.SaveEntryUseCase
import com.example.myapplication.domain.repository.EntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

data class AdminPanelState(
    val entries: List<Entry> = emptyList(),
    val stats: AdminStats = AdminStats(0, 0, 0, 0),
    val isLoading: Boolean = true
)

data class AddEditState(
    val entry: Entry? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val getAllEntries: GetAllEntriesUseCase,
    private val getEntryById: GetEntryByIdUseCase,
    private val saveEntry: SaveEntryUseCase,
    private val deleteEntry: DeleteEntryUseCase,
    private val repository: EntryRepository
) : ViewModel() {

    private val _panelState = MutableStateFlow(AdminPanelState())
    val panelState: StateFlow<AdminPanelState> = _panelState

    private val _editState = MutableStateFlow(AddEditState())
    val editState: StateFlow<AddEditState> = _editState

    init {
        loadPanel()
    }

    private fun loadPanel() {
        viewModelScope.launch {
            getAllEntries().collect { entries ->
                val stats = repository.getAdminStats()
                _panelState.value = AdminPanelState(
                    entries = entries,
                    stats = stats,
                    isLoading = false
                )
            }
        }
    }

    fun loadEntry(id: String?) {
        viewModelScope.launch {
            if (id != null) {
                val entry = getEntryById(id)
                _editState.value = AddEditState(entry = entry)
            } else {
                _editState.value = AddEditState()
            }
        }
    }

    fun save(
        id: String?,
        title: String,
        category: String,
        shortDesc: String,
        fullDesc: String,
        code: String,
        tags: List<String>,
        related: List<String>,
        isPublished: Boolean
    ) {
        viewModelScope.launch {
            _editState.value = _editState.value.copy(isLoading = true)
            val entryId = if (id.isNullOrBlank()) UUID.randomUUID().toString() else id
            val entry = Entry(
                id = entryId,
                title = title,
                shortDescription = shortDesc,
                fullDescription = fullDesc,
                codeExample = code,
                category = category,
                tags = tags,
                relatedTerms = related,
                views = _editState.value.entry?.views ?: 0,
                updatedAt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date()),
                isPublished = isPublished
            )
            saveEntry(entry)
            _editState.value = AddEditState(isSaved = true)
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            deleteEntry(id)
        }
    }
}
