package com.example.a05_implementazionealgoritmo;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class AdapterForTestCard extends RecyclerView.Adapter<AdapterForTestCard.ViewHolder> {
    private ArrayList<Card_Test> my_dataset;
    private View view;
    private ViewHolder view_holder;
    private Context context;
    private ArrayList<Configurazione> configurations;
    private AlertDialog.Builder alert_dialog_builder;

    public AdapterForTestCard(ArrayList<Card_Test> my_dataset, Context context, ArrayList<Configurazione> configurations) {
        this.my_dataset = my_dataset;
        this.context = context;
        this.configurations = configurations;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView number, date, steps, notes;
        public ImageButton select;
        public LinearLayout layout;
        public Calendar calendar = Calendar.getInstance();
        public String month, day, hour, minute, second;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number=itemView.findViewById(R.id.number);
            date=itemView.findViewById(R.id.date);
            select=itemView.findViewById(R.id.select);
            steps=itemView.findViewById(R.id.steps);
            notes=itemView.findViewById(R.id.notes);
            layout=itemView.findViewById(R.id.layout);
        }
    }

    @NonNull
    @Override
    public AdapterForTestCard.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_card, parent, false);
        view_holder = new ViewHolder(view);
        return view_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.number.setText(String.valueOf(position+1)+". ");
        try {
            holder.calendar.setTimeInMillis(Long.valueOf(new JSONObject(my_dataset.get(position).getTest_values()).keys().next()));
            holder.month=String.valueOf(holder.calendar.get(Calendar.MONTH)+1);
            if(holder.calendar.get(Calendar.MONTH)<10) holder.month="0"+holder.month;
            holder.day=String.valueOf(holder.calendar.get(Calendar.DAY_OF_MONTH));
            if(holder.calendar.get(Calendar.DAY_OF_MONTH)<10) holder.day="0"+holder.day;
            holder.hour=String.valueOf(holder.calendar.get(Calendar.HOUR_OF_DAY));
            if(holder.calendar.get(Calendar.HOUR_OF_DAY)<10) holder.hour="0"+holder.hour;
            holder.minute=String.valueOf(holder.calendar.get(Calendar.MINUTE));
            if(holder.calendar.get(Calendar.MINUTE)<10) holder.minute="0"+holder.minute;
            holder.second=String.valueOf(holder.calendar.get(Calendar.SECOND));
            if(holder.calendar.get(Calendar.SECOND)<10) holder.second="0"+holder.second;
            holder.date.setText(holder.day+"/"+holder.month+"/"+String.valueOf(holder.calendar.get(Calendar.YEAR))+" - "+holder.hour+":"+holder.minute+":"+holder.second);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.steps.setText(context.getResources().getString(R.string.number_of_steps_counted)+": "+String.valueOf(my_dataset.get(position).getNumber_of_steps()));

        holder.notes.setText(String.valueOf(my_dataset.get(position).getAdditional_notes()));
        if(my_dataset.get(position).getAdditional_notes().equals(""))
            holder.notes.setVisibility(View.GONE);
        else
            holder.notes.setVisibility(View.VISIBLE);
        holder.select.setOnClickListener(v -> context.startActivity(new Intent(context, ConfigurationsComparison.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION).putExtra("configurations", configurations).putExtra("test", my_dataset.get(position).getTest_id())));

        holder.layout.setOnLongClickListener(v -> {
            ((Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
            alert_dialog_builder = new AlertDialog.Builder(context);
            alert_dialog_builder.setMessage(context.getString(R.string.delete));
            alert_dialog_builder.setPositiveButton(context.getResources().getString(R.string.yes), (dialog, id) -> {
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
