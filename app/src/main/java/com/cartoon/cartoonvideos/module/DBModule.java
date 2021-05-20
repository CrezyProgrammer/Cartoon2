package com.cartoon.cartoonvideos.module;
import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabase.Callback;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.cartoon.cartoonvideos.AppDB;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Module
@InstallIn({SingletonComponent.class})
public final class DBModule {
    public static AppDB appDB;


    @Provides
    @NotNull
    public final AppDB provideDatabase(@ApplicationContext @NotNull Context appContext) {
 appDB = Room.databaseBuilder(appContext, AppDB.class, "demo.db")
                .fallbackToDestructiveMigration().addCallback((new Callback() {
            public void onCreate(@NotNull SupportSQLiteDatabase db) {

                super.onCreate(db);
                appDB.pigeonDao();
            }
        })).build();

        return appDB;
    }


}
