package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.local.dao.EntryDao
import com.example.myapplication.data.local.entity.EntryEntity

@Database(
    entities = [EntryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao

    companion object {
        const val DATABASE_NAME = "it_dictionary.db"
    }
}
