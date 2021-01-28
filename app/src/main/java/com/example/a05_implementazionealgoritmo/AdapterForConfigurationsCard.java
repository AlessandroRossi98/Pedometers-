package com.example.a05_implementazionealgoritmo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Iterator;

public class AdapterForConfigurationsCard extends RecyclerView.Adapter<AdapterForConfigurationsCard.ViewHolder> {
    private ArrayList<Configurazione> my_dataset;
    private ArrayList<Integer> my_color_dataset;
    private ArrayList<Integer> my_passi_dataset;
    private ArrayList<LineDataSet> my_lines;
    private LineDataSet my_line_app;
    private Context context;
    private View view;
    private AdapterForConfigurationsCard.ViewHolder view_holder;
    private LineData set_of_lines;
    private LineData set_of_lines_app;
    private Integer index;
    private LineChart line_chart;
    private Iterator<ILineDataSet> my_iterator;

    public AdapterForConfigurationsCard(ArrayList<Configurazione> my_dataset, ArrayList<Integer> my_color_dataset, ArrayList<Integer> my_passi_dataset, ArrayList<LineDataSet> my_lines, Context context,LineData set_of_lines, LineChart line_chart) {
        this.my_dataset = my_dataset;
        this.my_color_dataset = my_color_dataset;
        this.my_passi_dataset = my_passi_dataset;
        this.my_lines = my_lines;
        this.context = context;
        this.set_of_lines = set_of_lines;
        this.line_chart = line_chart;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView modality, algorithm, filter, frequency, steps, number, steps_text;
        public CardView this_card;
        public LinearLayout card_linearlayout_id, this_layout, other_layout;
        public LinearLayout number_border;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            modality=itemView.findViewById(R.id.modality);
            algorithm=itemView.findViewById(R.id.algorithm);
            filter=itemView.findViewById(R.id.filter);
            frequency=itemView.findViewById(R.id.frequency);
            number=itemView.findViewById(R.id.number);
            number_border=itemView.findViewById(R.id.number_border);
            steps=itemView.findViewById(R.id.steps);
            steps_text=itemView.findViewById(R.id.steps_text);
            this_card=itemView.findViewById(R.id.this_card);
            card_linearlayout_id=itemView.findViewById(R.id.card_linearlayout_id);
            this_layout=itemView.findViewById(R.id.this_layout);
            other_layout=itemView.findViewById(R.id.other_layout);
        }
    }

    @NonNull
    @Override
    public AdapterForConfigurationsCard.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.configuration_card, parent, false);
        view_holder = new AdapterForConfigurationsCard.ViewHolder(view);
        return view_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.number.setText(String.valueOf(position+1));
        holder.number_border.setBackgroundTintList(ColorStateList.valueOf(my_color_dataset.get(position)));
        holder.steps.setText(String.valueOf(my_passi_dataset.get(position)));
        switch (my_dataset.get(position).getReal_time()){
            case 0:
                holder.modality.setText(context.getResources().getString(R.string.modality_italic)+context.getResources().getString(R.string.real_time));
                break;
            case 1:
                holder.modality.setText(context.getResources().getString(R.string.modality_italic)+context.getResources().getString(R.string.not_real_time));
                break;
        }
        switch (my_dataset.get(position).getAlgoritmo_di_riconoscimento()){
            case 0:
                holder.algorithm.setText(context.getResources().getString(R.string.step_recognition_algorithm_italic)+context.getResources().getString(R.string.peak));
                break;
            case 1:
                holder.algorithm.setText(context.getResources().getString(R.string.step_recognition_algorithm_italic)+context.getResources().getString(R.string.intersection));
                break;
        }
        switch (my_dataset.get(position).getFiltro()){
            case 0:
                holder.filter.setText(context.getResources().getString(R.string.filter_italic)+context.getResources().getString(R.string.bagilevi_filter));
                holder.frequency.setVisibility(View.GONE);
                break;
            case 1:
                holder.filter.setText(context.getResources().getString(R.string.filter_italic)+context.getResources().getString(R.string.low_pass_filter));
                holder.frequency.setVisibility(View.VISIBLE);
                switch (my_dataset.get(position).getFrequenza_di_taglio()){
                    case 0:
                        holder.frequency.setText(context.getResources().getString(R.string.cutoff_frequency_italic)+"2 Hz");
                        break;
                    case 1:
                        holder.frequency.setText(context.getResources().getString(R.string.cutoff_frequency_italic)+"3 Hz");
                        break;
                    case 2:
                        holder.frequency.setText(context.getResources().getString(R.string.cutoff_frequency_italic)+"10 Hz");
                        break;
                    case 3:
                        holder.frequency.setText(context.getResources().getString(R.string.cutoff_frequency_italic)+context.getResources().getString(R.string.sampling_rate));
                        break;
                }
                break;
            case 2:
                holder.filter.setText(context.getResources().getString(R.string.filter_italic)+context.getResources().getString(R.string.no_filter));
                holder.frequency.setVisibility(View.GONE);
                break;
            case 3:
                holder.filter.setText(context.getResources().getString(R.string.filter_italic)+context.getResources().getString(R.string.rotation_filter));
                holder.frequency.setVisibility(View.GONE);
                break;
        }
        holder.number_border.setOnClickListener(v -> highlightLine(position));
        holder.number.setOnClickListener(v -> highlightLine(position));
        holder.card_linearlayout_id.setOnClickListener(v -> highlightLine(position));
        holder.modality.setOnClickListener(v -> highlightLine(position));
        holder.algorithm.setOnClickListener(v -> highlightLine(position));
        holder.filter.setOnClickListener(v -> highlightLine(position));
        holder.frequency.setOnClickListener(v -> highlightLine(position));
        holder.steps.setOnClickListener(v -> highlightLine(position));
        holder.this_card.setOnClickListener(v -> highlightLine(position));
        holder.this_layout.setOnClickListener(v -> highlightLine(position));
        holder.other_layout.setOnClickListener(v -> highlightLine(position));
        holder.steps_text.setOnClickListener(v -> highlightLine(position));
    }

    private void highlightLine(Integer position){
        set_of_lines_app=new LineData();
        my_iterator = set_of_lines.getDataSets().iterator();
        while(my_iterator.hasNext())
            set_of_lines_app.addDataSet(my_iterator.next());
        set_of_lines.clearValues();
        index = set_of_lines_app.getDataSetCount();
        for(int i=0; i<index; i++){
            my_line_app= (LineDataSet) set_of_lines_app.getDataSetByIndex(i);
            if(i==position+1) {
                my_line_app.setLineWidth(3f);
                set_of_lines.addDataSet(my_line_app);
            }
            else {
                my_line_app.setLineWidth(1f);
                set_of_lines.addDataSet(my_line_app);
            }
        }
        line_chart.clear();
        line_chart.setData(set_of_lines);
        set_of_lines.notifyDataChanged();
        line_chart.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return my_dataset.size();
    }
}
