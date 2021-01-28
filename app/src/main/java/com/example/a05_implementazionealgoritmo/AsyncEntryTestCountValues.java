package com.example.a05_implementazionealgoritmo;

import android.os.AsyncTask;

import java.util.List;
import java.util.ListIterator;

public class AsyncEntryTestCountValues extends AsyncTask<Void, Void, Integer> {
    final MyDatabase supportDB = MainActivity.getDatabase();
    private List<EntityTest> list_of_tests;
    private ListIterator my_iterator;
    private Integer result=0;

    @Override
    protected Integer doInBackground(Void... voids) {
        list_of_tests =supportDB.databaseDao().getAllTests();
        my_iterator= list_of_tests.listIterator();
        while (my_iterator.hasNext()){
            my_iterator.next();
            result++;
        }
        return result;
    }
}
