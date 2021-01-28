package com.example.a05_implementazionealgoritmo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;

public class AsyncEntryTestInsertNew extends AsyncTask<String, Void, Void> {
    final MyDatabase supportDB = MainActivity.getDatabase();
    private EntityTest entity_test;
    private Context context;
    private String file_name, file_body;
    private Calendar calendar = Calendar.getInstance();
    private File directory;
    private FileWriter file_writer;
    private String month, day, hour, minute, second;


    public AsyncEntryTestInsertNew(Context context) {
        this.context=context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            calendar.setTimeInMillis(Long.valueOf(new JSONObject(strings[0]).keys().next()));
            month=String.valueOf(calendar.get(Calendar.MONTH)+1);
            if(calendar.get(Calendar.MONTH)<10) month="0"+month;
            day=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            if(calendar.get(Calendar.DAY_OF_MONTH)<10) day="0"+day;
            hour=String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
            if(calendar.get(Calendar.HOUR_OF_DAY)<10) hour="0"+hour;
            minute=String.valueOf(calendar.get(Calendar.MINUTE));
            if(calendar.get(Calendar.MINUTE)<10) minute="0"+minute;
            second=String.valueOf(calendar.get(Calendar.SECOND));
            if(calendar.get(Calendar.SECOND)<10) second="0"+second;
            file_name=String.valueOf(calendar.get(Calendar.YEAR))+"-"+month+"-"+day+"_"+hour+":"+minute+":"+second+".txt";

            entity_test=new EntityTest();
            entity_test.test_values=strings[0];
            entity_test.number_of_steps=Integer.parseInt(strings[1]);
            entity_test.additional_notes=strings[2];
            entity_test.file_name=file_name;
            supportDB.databaseDao().insertTest(entity_test);

            file_body="{\"number_of_steps\":\""+strings[1]+"\", \"additional_notes\":\""+strings[2]+"\", \"test_values\":"+strings[0]+"}";

            directory = new File(context.getFilesDir(), "");
            if(!directory.exists()) directory.mkdir();

            try {
                File new_file=new File(directory, file_name);
                new_file.setReadable(true);
                file_writer = new FileWriter(new_file);
                file_writer.append(file_body);
                file_writer.flush();
                file_writer.close();
            } catch (Exception e){
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
