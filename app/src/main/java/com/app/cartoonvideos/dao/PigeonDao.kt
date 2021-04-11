package com.app.cartoonvideos.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.cartoonvideos.entity.Video

@Dao
interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(video: Video)
    @Query("SELECT * FROM Video WHERE category=:b")
    fun getAllVideo(b:String):LiveData<List<Video>>
}