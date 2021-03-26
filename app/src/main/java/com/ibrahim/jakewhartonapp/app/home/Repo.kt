package com.ibrahim.jakewhartonapp.app.home

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Repo(
    @PrimaryKey val id : Int ?= 0,
    val name : String ?= "",
    val description : String ? = "",
    val watchers : Int ?= 0,
    val url : String ?= ""
)
