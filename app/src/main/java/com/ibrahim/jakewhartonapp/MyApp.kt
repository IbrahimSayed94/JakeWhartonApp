package com.ibrahim.jakewhartonapp

import androidx.multidex.MultiDexApplication
import androidx.room.Room
import com.ibrahim.jakewhartonapp.app.home.database.AppDatabase

class MyApp : MultiDexApplication() {

    companion object {
        lateinit var appDataBase: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        provideDatabase()
    } // fun of onCreate

    private fun provideDatabase() {
       appDataBase = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "jakewhartonapp.db")
           .allowMainThreadQueries().build()

    }  // fun of provideDatabase
}