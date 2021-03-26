package com.ibrahim.jakewhartonapp.app.home.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ibrahim.jakewhartonapp.app.home.Repo

@Dao
interface RepoDao {
    @Query("SELECT * FROM repo")
    fun getRepos(): LiveData<List<Repo>>

    @Insert
    fun insert(repoList : List<Repo>)

    @Query("DELETE FROM repo")
    fun delete()

}