package com.cartoon.cartoonvideos;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.cartoon.cartoonvideos.dao.VideoDao;
import com.cartoon.cartoonvideos.entity.Video;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;


@Database(
        entities = {Video.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDB extends RoomDatabase {
    @NotNull
    public abstract VideoDao pigeonDao();
}
