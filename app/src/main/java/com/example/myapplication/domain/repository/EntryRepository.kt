package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.AdminStats
import com.example.myapplication.domain.model.Entry
import kotlinx.coroutines.flow.Flow

interface EntryRepository {
    fun getAllEntries(): Flow<List<Entry>>
    fun getPopularEntries(): Flow<List<Entry>>
    fun getRecentEntries(): Flow<List<Entry>>
    fun getEntriesByCategory(category: String): Flow<List<Entry>>
    fun searchEntries(query: String): Flow<List<Entry>>
    fun getBookmarkedEntries(): Flow<List<Entry>>
    suspend fun getEntryById(id: String): Entry?
    suspend fun insertEntry(entry: Entry)
    suspend fun updateEntry(entry: Entry)
    suspend fun deleteEntry(id: String)
    suspend fun toggleBookmark(entryId: String)
    suspend fun getAdminStats(): AdminStats
    suspend fun syncWithRemote()
}
