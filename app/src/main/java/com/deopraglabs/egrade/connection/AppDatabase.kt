package com.deopraglabs.egrade.connection

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.deopraglabs.egrade.model.User
import com.deopraglabs.egrade.util.DataConverter

@Database(
    entities = [
        User::class
    ],
    version = 1)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {

}