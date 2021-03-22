package com.cartoon.funnyvideos.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cartoon.funnyvideos.entity.Video

@Dao
interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(video: Video)
    @Query("SELECT * FROM Video WHERE isPopular=:b")
    fun getAllVideo(b: Boolean):LiveData<List<Video>>
}