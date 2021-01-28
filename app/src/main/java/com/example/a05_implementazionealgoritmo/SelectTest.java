package com.example.a05_implementazionealgoritmo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;

public class SelectTest extends AppCompatActivity {
    private ArrayList<Configurazione> configurations;
    private ArrayList<Card_Test> my_dataset;
    private AdapterForTestCard my_adapter;
    private RecyclerView recycler_view;
    private List<EntityTest> list_of_tests;
    private ListIterator my_iterator;
    private EntityTest current_test_from_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_test);
        configurations = (ArrayList<Configurazione>) getIntent().getSerializableExtra("configurations");

        recycler_view=findViewById(R.id.recycler_view);

        my_dataset = new ArrayList<Card_Test>();
        my_adapter = new AdapterForTestCard(my_dataset, this, configurations);
        recycler_view.setAdapter(my_adapter);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        try {
            list_of_tests = new AsyncEntryTestGetAll().execute().get();
            my_iterator = list_of_tests.listIterator();
            while (my_iterator.hasNext()) {
                current_test_from_list = (EntityTest) my_iterator.next();
                my_dataset.add(new Card_Test(String.valueOf(current_test_from_list.test_id), current_test_from_list.test_values, current_test_from_list.number_of_steps, current_test_from_list.additional_notes, current_test_from_list.file_name));
            }
            my_adapter.notifyDataSetChanged();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }
}
