package com.example.a05_implementazionealgoritmo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {EntityTest.class}, version = 1)
abstract class MyDatabase extends RoomDatabase {
    public abstract DatabaseDao databaseDao();
}
