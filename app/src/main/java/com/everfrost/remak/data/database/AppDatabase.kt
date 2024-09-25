package com.everfrost.remak.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.everfrost.remak.data.Dao.DocumentDao
import com.everfrost.remak.data.converter.Converters
import com.everfrost.remak.data.entity.Document


@Database(entities = [Document::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao

    companion object {
        const val DATABASE_NAME = "remak_db"
    }
}