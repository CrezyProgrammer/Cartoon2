package com.cartoon.cartoonvideos.repo

import com.cartoon.cartoonvideos.AppDB
import com.cartoon.cartoonvideos.entity.Video
import java.util.concurrent.Executors
import javax.inject.Inject

class DataRepository @Inject constructor(appDB: AppDB) {

    private val wordDao = appDB.pigeonDao()

    private val executor = Executors.newSingleThreadExecutor()

    fun insert(word: Video) {
        executor.execute {
            wordDao.insert(word)
        }
    }

    fun getAllVideo(b:String) = wordDao.getAllVideo(b)

}