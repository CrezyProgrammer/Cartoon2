package com.app.cartoonvideos

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.cartoonvideos.dao.VideoDao
import com.app.cartoonvideos.entity.Video

@Database(entities = [Video::class],version = 1,exportSchema = false)
abstract class AppDB:RoomDatabase() {
    abstract  fun  pigeonDao(): VideoDao
}