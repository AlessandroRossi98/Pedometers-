package com.example.a05_implementazionealgoritmo;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class AsyncEntryTestGetAll extends AsyncTask<Void, Void, List<EntityTest>> {
    final MyDatabase supportDB = MainActivity.getDatabase();

    @Override
    protected List<EntityTest> doInBackground(Void... voids) {
        return supportDB.databaseDao().getAllTests();
    }
}
