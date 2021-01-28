package com.example.a05_implementazionealgoritmo;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class AsyncEntryTestGetFromId extends AsyncTask<Integer, Void, EntityTest> {
    final MyDatabase supportDB = MainActivity.getDatabase();

    @Override
    protected EntityTest doInBackground(Integer... integers) {
        return supportDB.databaseDao().getTestFromId(integers[0]);
    }
}
