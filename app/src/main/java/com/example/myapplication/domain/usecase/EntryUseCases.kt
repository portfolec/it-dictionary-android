package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.Entry
import com.example.myapplication.domain.repository.EntryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllEntriesUseCase @Inject constructor(
    private val repository: EntryRepository
) {
    operator fun invoke(): Flow<List<Entry>> = repository.getAllEntries()
}

class GetPopularEntriesUseCase @Inject constructor(
    private val repository: EntryRepository
) {
    operator fun invoke(): Flow<List<Entry>> = repository.getPopularEntries()
}

class GetRecentEntriesUseCase @Inject constructor(
    private val repository: EntryRepository
) {
    operator fun invoke(): Flow<List<Entry>> = repository.getRecentEntries()
}

class SearchEntriesUseCase @Inject constructor(
    private val repository: EntryRepository
) {
    operator fun invoke(query: String): Flow<List<Entry>> = repository.searchEntries(query)
}

class GetEntriesByCategoryUseCase @Inject constructor(
    private val repository: EntryRepository
) {
    operator fun invoke(category: String): Flow<List<Entry>> =
        repository.getEntriesByCategory(category)
}

class GetBookmarkedEntriesUseCase @Inject constructor(
    private val repository: EntryRepository
) {
    operator fun invoke(): Flow<List<Entry>> = repository.getBookmarkedEntries()
}

class GetEntryByIdUseCase @Inject constructor(
    private val repository: EntryRepository
) {
    suspend operator fun invoke(id: String): Entry? = repository.getEntryById(id)
}

class ToggleBookmarkUseCase @Inject constructor(
    private val repository: EntryRepository
) {
    suspend operator fun invoke(entryId: String) = repository.toggleBookmark(entryId)
}

class SaveEntryUseCase @Inject constructor(
    private val repository: EntryRepository
) {
    suspend operator fun invoke(entry: Entry) {
        if (entry.id.isEmpty()) {
            repository.insertEntry(entry)
        } else {
            repository.updateEntry(entry)
        }
    }
}

class DeleteEntryUseCase @Inject constructor(
    private val repository: EntryRepository
) {
    suspend operator fun invoke(id: String) = repository.deleteEntry(id)
}
