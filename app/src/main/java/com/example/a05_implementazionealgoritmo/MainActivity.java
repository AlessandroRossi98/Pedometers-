package com.example.a05_implementazionealgoritmo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder alert_dialog_builder;
    private Button enter_configuration, register_new_test, compare_configurations, import_test, send_test;
    private Integer number_of_tests;
    public static MyDatabase my_database;
    public static Integer NUMBER_OF_DOTS_IN_GRAPH =300;

    private Intent choose_file_intent;
    private InputStream my_input_stream;
    private BufferedReader my_buffered_reader;
    private StringBuilder my_string_builder;
    private String line;
    private JSONObject json_object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        my_database = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "tests.db").build();

        enter_configuration=findViewById(R.id.enter_configuration);
        register_new_test=findViewById(R.id.register_new_test);
        compare_configurations=findViewById(R.id.compare_configurations);
        import_test=findViewById(R.id.import_test);
        send_test =findViewById(R.id.send_test);
        number_of_tests=0;

        try {
            number_of_tests = new AsyncEntryTestCountValues().execute().get();
            if(number_of_tests>0) {
                compare_configurations.setEnabled(true);
                send_test.setEnabled(true);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        enter_configuration.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LiveTesting.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)));
        register_new_test.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), NewTest.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)));
        compare_configurations.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CompareConfigurations.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)));
        import_test.setOnClickListener(v -> {
            choose_file_intent =new Intent(Intent.ACTION_GET_CONTENT);
            choose_file_intent.addCategory(Intent.CATEGORY_OPENABLE).setType("text/plain");
            startActivityForResult(choose_file_intent, 0);
        });
        send_test.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SendTest.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)));

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode!=RESULT_OK) return;
        my_input_stream=null;
        try {
            my_input_stream = getContentResolver().openInputStream(data.getData());

            my_buffered_reader=new BufferedReader(new InputStreamReader(my_input_stream));
            my_string_builder=new StringBuilder();

            while ((line = my_buffered_reader.readLine()) != null) {
                my_string_builder.append(line).append('\n');
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                my_input_stream.close();
                json_object=new JSONObject(my_string_builder.toString());
                new AsyncEntryTestInsertNew(this).execute(json_object.getString("test_values"), json_object.getString("number_of_steps"), json_object.getString("additional_notes"));
                Toast.makeText(this, getResources().getString(R.string.test_imported), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        alert_dialog_builder = new AlertDialog.Builder(this);
        alert_dialog_builder.setMessage(getResources().getString(R.string.exit));
        alert_dialog_builder.setPositiveButton(getResources().getString(R.string.ok), (dialog, id) -> startActivity((new Intent(Intent.ACTION_MAIN)).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));
        alert_dialog_builder.setNegativeButton(getResources().getString(R.string.cancel), null);
        alert_dialog_builder.create().show();
    }

    public static MyDatabase getDatabase() {
        return my_database;
    }
}