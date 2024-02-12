package com.deopraglabs.egrade.connection

import android.content.Context
import androidx.room.Room

object DatabaseManager  {
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "egrade_db"
            ).build()
        }
        return instance!!
    }
}