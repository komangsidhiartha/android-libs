package com.git.dabang.helper

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.git.dabang.database.FormDataDao
import com.git.dabang.database.table.FormDataTable
import com.mamikos.mamiagent.BuildConfig

/**
 * Created by Dedi Dot on 9/19/2018.
 * Happy Coding!
 */

@Database(entities = [(FormDataTable::class)], version = BuildConfig.DATABASE_VERSION, // if any change structure database, please increase this version
        exportSchema = false) abstract class AppDatabase : RoomDatabase() {
    abstract fun formDataDao(): FormDataDao
}