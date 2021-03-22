package com.cartoon.funnyvideos.repo

import com.cartoon.funnyvideos.AppDB
import com.cartoon.funnyvideos.entity.Video
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

    fun getAllVideo(b: Boolean) = wordDao.getAllVideo(b)
    fun getPopularVideo(b: Boolean) = wordDao.getAllVideo(b)

}