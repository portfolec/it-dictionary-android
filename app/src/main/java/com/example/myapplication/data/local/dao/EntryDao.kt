package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.EntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {

    @Query("SELECT * FROM entries WHERE isPublished = 1 ORDER BY views DESC")
    fun getAllEntries(): Flow<List<EntryEntity>>

    @Query("SELECT * FROM entries WHERE isPublished = 1 ORDER BY views DESC LIMIT 5")
    fun getPopularEntries(): Flow<List<EntryEntity>>

    @Query("SELECT * FROM entries WHERE isPublished = 1 ORDER BY updatedAt DESC LIMIT 5")
    fun getRecentEntries(): Flow<List<EntryEntity>>

    @Query("SELECT * FROM entries WHERE category = :category AND isPublished = 1 ORDER BY title ASC")
    fun getEntriesByCategory(category: String): Flow<List<EntryEntity>>

    @Query("SELECT * FROM entries WHERE (title LIKE '%' || :query || '%' OR shortDescription LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%') AND isPublished = 1")
    fun searchEntries(query: String): Flow<List<EntryEntity>>

    @Query("SELECT * FROM entries WHERE isBookmarked = 1")
    fun getBookmarkedEntries(): Flow<List<EntryEntity>>

    @Query("SELECT * FROM entries WHERE isPublished = 1")
    suspend fun getAllEntriesOnce(): List<EntryEntity>

    @Query("SELECT * FROM entries WHERE id = :id")
    suspend fun getEntryById(id: String): EntryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: EntryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<EntryEntity>)

    @Update
    suspend fun updateEntry(entry: EntryEntity)

    @Query("DELETE FROM entries WHERE id = :id")
    suspend fun deleteEntry(id: String)

    @Query("UPDATE entries SET isBookmarked = CASE WHEN isBookmarked = 1 THEN 0 ELSE 1 END WHERE id = :entryId")
    suspend fun toggleBookmark(entryId: String)

    @Query("DELETE FROM entries WHERE isBookmarked = 0")
    suspend fun deleteNonBookmarked()

    @Query("SELECT COUNT(*) FROM entries")
    suspend fun getTotalCount(): Int

    @Query("SELECT COUNT(DISTINCT category) FROM entries")
    suspend fun getCategoryCount(): Int
}
