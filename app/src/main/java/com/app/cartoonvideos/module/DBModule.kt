package com.app.cartoonvideos.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.cartoonvideos.AppDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    lateinit var appDB: AppDB

    private val executor = Executors.newSingleThreadExecutor()

    @Provides
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): AppDB {
        appDB = Room.databaseBuilder(
            appContext,
            AppDB::class.java,
            "demo.db"
        ).fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val dao = appDB.pigeonDao()

                }
            })
            .build()

        return appDB
    }

}