package com.gksoftwaresolutions.comicviewer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gksoftwaresolutions.comicviewer.R
import com.gksoftwaresolutions.comicviewer.data.local.dao.ItemMarvelDao
import com.gksoftwaresolutions.comicviewer.data.local.entities.ItemMarvel

@Database(entities = [ItemMarvel::class], version = 1, exportSchema = false)
abstract class DatabaseConfig : RoomDatabase() {
    abstract fun itemMarvelDao(): ItemMarvelDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseConfig? = null

        fun getInstance(context: Context): DatabaseConfig {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseConfig::class.java,
                    context.resources.getString(R.string.database_name)
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}