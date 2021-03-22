package com.cartoon.funnyvideos.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Video(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    var title: String = "",
    var duration: String = "",
    var isPopular:Boolean=false
)
