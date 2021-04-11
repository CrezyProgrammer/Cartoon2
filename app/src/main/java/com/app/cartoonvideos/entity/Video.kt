package com.app.cartoonvideos.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Video(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    var title: String = "",
    var duration: String = "",
    var category:String="",
    var date:String="0 month",
    var views:String="0k"
)
