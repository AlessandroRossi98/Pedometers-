package com.example.a05_implementazionealgoritmo;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class AdapterForSendTestCard extends RecyclerView.Adapter<AdapterForSendTestCard.ViewHolder> {
    private Context context;
    private ArrayList<Card_Test> my_dataset;
    private View view;
    private AdapterForSendTestCard.ViewHolder view_holder;
    private Button send_test;
    private Integer selected_tests;
    private AlertDialog.Builder alert_dialog_builder;
    private Boolean binding;

    public AdapterForSendTestCard(Context context, ArrayList<Card_Test> my_dataset, Integer selected_tests, Button send_test) {
        this.my_dataset = my_dataset;
        this.context = context;
        this.selected_tests = selected_tests;
        this.send_test = send_test;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox selected;
        public TextView file_path_name, notes, number_of_steps;
        public LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selected=itemView.findViewById(R.id.selected);
            file_path_name=itemView.findViewById(R.id.file_path_name);
            notes=itemView.findViewById(R.id.notes);
            number_of_steps=itemView.findViewById(R.id.number_of_steps);
            layout=itemView.findViewById(R.id.layout);
        }
    }

    @NonNull
    @Override
    public AdapterForSendTestCard.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_test_card, parent, false);
        view_holder = new AdapterForSendTestCard.ViewHolder(view);
        return view_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.file_path_name.setText(my_dataset.get(position).getFile_path_name());
        holder.number_of_steps.setText(context.getResources().getString(R.string.number_of_steps)+" "+String.valueOf(my_dataset.get(position).getNumber_of_steps()));
        holder.notes.setText(String.valueOf(my_dataset.get(position).getAdditional_notes()));
        if(my_dataset.get(position).getAdditional_notes().equals(""))
            holder.notes.setVisibility(View.GONE);
        else
            holder.notes.setVisibility(View.VISIBLE);
        binding=true;
        if(my_dataset.get(position).getSelected())
            holder.selected.setChecked(true);
        else
            holder.selected.setChecked(false);
        binding=false;
        holder.selected.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!binding) {
                my_dataset.get(position).setSelected(isChecked);
                notifyDataSetChanged();
                if (isChecked) selected_tests++;
                else selected_tests--;
                if (selected_tests > 0) send_test.setEnabled(true);
                else send_test.setEnabled(false);
            }
        });
        holder.layout.setOnLongClickListener(v -> {
            ((Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
            alert_dialog_builder = new AlertDialog.Builder(context);
            alert_dialog_builder.setMessage(context.getString(R.string.delete));
            alert_dialog_builder.setPositiveButton(context.getResources().getString(R.string.yes), (dialog, id) -> {
                if (my_dataset.get(position).getSelected()) selected_tests--;
                if (selected_tests>0) send_test.setEnabled(true);
                else send_test.setEnabled(false);
                new File(context.getFilesDir(), my_dataset.get(position).getFile_path_name()).delete();
                new AsyncEntryTestDelete().execute(Integer.parseInt(my_dataset.get(position).getTest_id()));
                my_dataset.remove(position);
                notifyItemRemoved(position);
            });
            alert_dialog_builder.setNegativeButton(context.getResources().getString(R.string.No), null);
            alert_dialog_builder.create().show();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return my_dataset.size();
    }
}
