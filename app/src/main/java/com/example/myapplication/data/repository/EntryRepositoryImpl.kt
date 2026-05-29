package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.EntryDao
import com.example.myapplication.data.local.entity.EntryEntity
import com.example.myapplication.data.local.entity.toEntity
import com.example.myapplication.data.remote.api.DictionaryApi
import com.example.myapplication.domain.model.AdminStats
import com.example.myapplication.domain.model.Entry
import com.example.myapplication.domain.repository.EntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EntryRepositoryImpl @Inject constructor(
    private val dao: EntryDao,
    private val api: DictionaryApi
) : EntryRepository {

    override fun getAllEntries(): Flow<List<Entry>> =
        dao.getAllEntries().map { list -> list.map { it.toDomain() } }

    override fun getPopularEntries(): Flow<List<Entry>> =
        dao.getPopularEntries().map { list -> list.map { it.toDomain() } }

    override fun getRecentEntries(): Flow<List<Entry>> =
        dao.getRecentEntries().map { list -> list.map { it.toDomain() } }

    override fun getEntriesByCategory(category: String): Flow<List<Entry>> =
        dao.getEntriesByCategory(category).map { list -> list.map { it.toDomain() } }

    override fun searchEntries(query: String): Flow<List<Entry>> =
        dao.searchEntries(query).map { list -> list.map { it.toDomain() } }

    override fun getBookmarkedEntries(): Flow<List<Entry>> =
        dao.getBookmarkedEntries().map { list -> list.map { it.toDomain() } }

    override suspend fun getEntryById(id: String): Entry? =
        dao.getEntryById(id)?.toDomain()

    override suspend fun insertEntry(entry: Entry) =
        dao.insertEntry(entry.toEntity())

    override suspend fun updateEntry(entry: Entry) =
        dao.updateEntry(entry.toEntity())

    override suspend fun deleteEntry(id: String) =
        dao.deleteEntry(id)

    override suspend fun toggleBookmark(entryId: String) =
        dao.toggleBookmark(entryId)

    override suspend fun getAdminStats(): AdminStats {
        return try {
            val stats = api.getAdminStats()
            AdminStats(
                totalEntries = stats["total_entries"] ?: dao.getTotalCount(),
                totalCategories = stats["total_categories"] ?: dao.getCategoryCount(),
                totalUsers = stats["total_users"] ?: 0,
                todayViews = stats["today_views"] ?: 0
            )
        } catch (e: Exception) {
            AdminStats(
                totalEntries = dao.getTotalCount(),
                totalCategories = dao.getCategoryCount(),
                totalUsers = 0,
                todayViews = 0
            )
        }
    }

    override suspend fun syncWithRemote() {
        try {
            val remoteEntries = api.getAllEntries()

            // Защита: если сервер вернул пустой список — не трогаем локальные данные
            if (remoteEntries.size < 3) return

            // Удаляем незакладкованные локальные данные
            dao.deleteNonBookmarked()

            // Вставляем актуальные данные с сервера
            val entities = remoteEntries.map { dto ->
                EntryEntity(
                    id = dto.id,
                    title = dto.title,
                    shortDescription = dto.shortDescription,
                    fullDescription = dto.fullDescription,
                    codeExample = dto.codeExample,
                    category = dto.category,
                    tags = dto.tags.joinToString(","),
                    relatedTerms = dto.relatedTerms.joinToString(","),
                    views = dto.views,
                    updatedAt = dto.updatedAt,
                    isPublished = dto.isPublished,
                    isBookmarked = false
                )
            }
            dao.insertEntries(entities)
        } catch (_: Exception) {
            // Если сервер недоступен — работаем с локальным кэшем Room
        }
    }
}
