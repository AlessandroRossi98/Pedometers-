package com.example.a05_implementazionealgoritmo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;

public class SendTest extends AppCompatActivity {
    private Intent share_file_intent;
    private ArrayList<Uri> files_to_share;
    private ArrayList<Card_Test> my_dataset;
    private AdapterForSendTestCard my_adapter;
    private RecyclerView recycler_view;
    private Button send_test;
    private Integer i, selected_tests;
    private List<EntityTest> list_of_tests;
    private ListIterator my_iterator;
    private EntityTest current_test_from_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_test);

        recycler_view=findViewById(R.id.recycler_view);
        send_test=findViewById(R.id.send_test);

        selected_tests=0;
        my_dataset = new ArrayList<Card_Test>();

        try {
            list_of_tests = new AsyncEntryTestGetAll().execute().get();
            my_iterator = list_of_tests.listIterator();
            while (my_iterator.hasNext()) {
                current_test_from_list = (EntityTest) my_iterator.next();
                my_dataset.add(new Card_Test(String.valueOf(current_test_from_list.test_id), current_test_from_list.test_values, current_test_from_list.number_of_steps, current_test_from_list.additional_notes, current_test_from_list.file_name));
            }
            my_adapter = new AdapterForSendTestCard(this, my_dataset, selected_tests, send_test);
            recycler_view.setAdapter(my_adapter);
            recycler_view.setHasFixedSize(true);
            recycler_view.setLayoutManager(new LinearLayoutManager(this));

            send_test.setOnClickListener(v -> {
                files_to_share = new ArrayList<Uri>();
                for (i=0; i<my_dataset.size(); i++)
                    if(my_dataset.get(i).getSelected())
                        files_to_share.add(FileProvider.getUriForFile(getApplicationContext(), "com.example.a05_implementazionealgoritmo.fileprovider", new File(getApplicationContext().getFilesDir(), my_dataset.get(i).getFile_path_name())));
                share_file_intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                share_file_intent.setType("text/*");
                share_file_intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files_to_share);
                startActivity(share_file_intent);
            });
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
