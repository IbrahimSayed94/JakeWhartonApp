package com.ibrahim.jakewhartonapp.app.home.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ibrahim.jakewhartonapp.app.home.Repo

@Database(entities = [Repo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}