package com.example.a05_implementazionealgoritmo;

import android.os.AsyncTask;

public class AsyncEntryTestDelete extends AsyncTask<Integer, Void, Void> {
    final MyDatabase supportDB = MainActivity.getDatabase();

    @Override
    protected Void doInBackground(Integer... integers) {
        supportDB.databaseDao().deleteTest(integers[0]);
        return null;
    }
}
