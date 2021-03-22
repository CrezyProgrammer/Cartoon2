package com.cartoon.funnyvideos

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cartoon.funnyvideos.dao.VideoDao
import com.cartoon.funnyvideos.entity.Video

@Database(entities = [Video::class],version = 1,exportSchema = false)
abstract class AppDB:RoomDatabase() {
    abstract  fun  pigeonDao(): VideoDao
}