package com.example.a05_implementazionealgoritmo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.math.BigDecimal;

public class EnterSettingsFragment extends Fragment {

    private Configurazione configurazione;
    private Boolean first=true;

    private ScrollView scroll_view;
    private RadioButton sampling_twenty, sampling_forty, sampling_fifty, sampling_hundred, sampling_max, modality_real_time, modality_not_real_time, recognition_peak, recognition_intersection;
    private RadioButton filter_bagilevi, filter_low_pass, no_filter, filter_rotation, cutoff_two, cutoff_three, cutoff_ten, cutoff_divided_fifty;
    private LinearLayout cutoff_frequency_layout, layout_sampling_rate;
    private TextView text_sampling_rate;
    private Boolean show_sampling_rate;
    private View first_view;

    public EnterSettingsFragment(){
        this.configurazione=new Configurazione();
        this.show_sampling_rate=true;
    }

    public EnterSettingsFragment(Configurazione configurazione){
        this.configurazione=configurazione; //ho il riferimento all'oggetto di MainActivity
        this.show_sampling_rate=true;
    }

    public EnterSettingsFragment(Configurazione configurazione, boolean show_sampling_rate) {
        this.configurazione=configurazione; //ho il riferimento all'oggetto di MainActivity
        this.show_sampling_rate=show_sampling_rate;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.enter_settings_fragment, container, false);

        scroll_view=root.findViewById(R.id.scroll_view);
        sampling_twenty=root.findViewById(R.id.sampling_twenty);
        sampling_forty=root.findViewById(R.id.sampling_forty);
        sampling_fifty=root.findViewById(R.id.sampling_fifty);
        sampling_hundred=root.findViewById(R.id.sampling_hundred);
        sampling_max=root.findViewById(R.id.sampling_max);
        modality_real_time=root.findViewById(R.id.modality_real_time);
        modality_not_real_time=root.findViewById(R.id.modality_not_real_time);
        recognition_peak=root.findViewById(R.id.recognition_peak);
        recognition_intersection=root.findViewById(R.id.recognition_intersection);
        filter_bagilevi=root.findViewById(R.id.filter_bagilevi);
        filter_low_pass=root.findViewById(R.id.filter_low_pass);
        no_filter=root.findViewById(R.id.no_filter);
        filter_rotation=root.findViewById(R.id.filter_rotation);
        cutoff_frequency_layout=root.findViewById(R.id.cutoff_frequency_layout);
        cutoff_two=root.findViewById(R.id.cutoff_two);
        cutoff_three=root.findViewById(R.id.cutoff_three);
        cutoff_ten=root.findViewById(R.id.cutoff_ten);
        cutoff_divided_fifty=root.findViewById(R.id.cutoff_divided_fifty);
        text_sampling_rate=root.findViewById(R.id.text_sampling_rate);
        layout_sampling_rate=root.findViewById(R.id.layout_sampling_rate);
        first_view=root.findViewById(R.id.first_view);

        if(!show_sampling_rate){
            text_sampling_rate.setVisibility(View.GONE);
            layout_sampling_rate.setVisibility(View.GONE);
            first_view.setVisibility(View.GONE);
        }
        else{
            text_sampling_rate.setVisibility(View.VISIBLE);
            layout_sampling_rate.setVisibility(View.VISIBLE);
            first_view.setVisibility(View.VISIBLE);
        }

        sampling_twenty.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                configurazione.setFrequenza_di_campionamento(0);
                sampling_forty.setChecked(false);
                sampling_fifty.setChecked(false);
                sampling_hundred.setChecked(false);
                sampling_max.setChecked(false);
            }
        });
        sampling_forty.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                configurazione.setFrequenza_di_campionamento(1);
                sampling_twenty.setChecked(false);
                sampling_fifty.setChecked(false);
                sampling_hundred.setChecked(false);
                sampling_max.setChecked(false);
            }
        });
        sampling_fifty.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                configurazione.setFrequenza_di_campionamento(2);
                sampling_twenty.setChecked(false);
                sampling_forty.setChecked(false);
                sampling_hundred.setChecked(false);
                sampling_max.setChecked(false);
            }
        });
        sampling_hundred.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                configurazione.setFrequenza_di_campionamento(3);
                sampling_twenty.setChecked(false);
                sampling_forty.setChecked(false);
                sampling_fifty.setChecked(false);
                sampling_max.setChecked(false);
            }
        });
        sampling_max.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                configurazione.setFrequenza_di_campionamento(4);
                sampling_twenty.setChecked(false);
                sampling_forty.setChecked(false);
                sampling_fifty.setChecked(false);
                sampling_hundred.setChecked(false);
            }
        });

        modality_real_time.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                configurazione.setReal_time(0);
                modality_not_real_time.setChecked(false);
            }
        });
        modality_not_real_time.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                configurazione.setReal_time(1);
                modality_real_time.setChecked(false);
            }
        });

        recognition_peak.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                configurazione.setAlgoritmo_di_riconoscimento(0);
                recognition_intersection.setChecked(false);
            }
        });
        recognition_intersection.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                configurazione.setAlgoritmo_di_riconoscimento(1);
                recognition_peak.setChecked(false);
            }
        });

        filter_bagilevi.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                configurazione.setFiltro(0);
                filter_low_pass.setChecked(false);
                no_filter.setChecked(false);
                filter_rotation.setChecked(false);
                cutoff_frequency_layout.setVisibility(View.GONE);
            }
        });
        filter_low_pass.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                configurazione.setFiltro(1);
                configurazione.setIndividuazione_threshold(BigDecimal.valueOf(5));
                filter_bagilevi.setChecked(false);
                no_filter.setChecked(false);
                filter_rotation.setChecked(false);
                cutoff_frequency_layout.setVisibility(View.VISIBLE);
                if(!first)
                    scroll_view.post(() -> scroll_view.fullScroll(View.FOCUS_DOWN));
                else
                    first=false;
            }
        });
        no_filter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                configurazione.setFiltro(2);
                configurazione.setIndividuazione_threshold(BigDecimal.valueOf(5));
                filter_bagilevi.setChecked(false);
                filter_low_pass.setChecked(false);
                filter_rotation.setChecked(false);
                cutoff_frequency_layout.setVisibility(View.GONE);
            }
        });
        filter_rotation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                configurazione.setFiltro(3);
                configurazione.setIndividuazione_threshold(BigDecimal.valueOf(8));
                filter_bagilevi.setChecked(false);
                filter_low_pass.setChecked(false);
                no_filter.setChecked(false);
                cutoff_frequency_layout.setVisibility(View.GONE);
            }
        });

        cutoff_two.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                configurazione.setFrequenza_di_taglio(0);
                cutoff_three.setChecked(false);
                cutoff_ten.setChecked(false);
                cutoff_divided_fifty.setChecked(false);
            }
        });
        cutoff_three.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                configurazione.setFrequenza_di_taglio(1);
                cutoff_two.setChecked(false);
                cutoff_ten.setChecked(false);
                cutoff_divided_fifty.setChecked(false);
            }
        });
        cutoff_ten.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                configurazione.setFrequenza_di_taglio(2);
                cutoff_two.setChecked(false);
                cutoff_three.setChecked(false);
                cutoff_divided_fifty.setChecked(false);
            }
        });
        cutoff_divided_fifty.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                configurazione.setFrequenza_di_taglio(3);
                cutoff_two.setChecked(false);
                cutoff_three.setChecked(false);
                cutoff_ten.setChecked(false);
            }
        });

        sampling_max.setChecked(true);
        modality_real_time.setChecked(true);
        recognition_intersection.setChecked(true);
        filter_low_pass.setChecked(true);
        cutoff_divided_fifty.setChecked(true);

        return root;
    }

    public void disableAllViews(){
        scroll_view.setOnTouchListener((v, event) -> true);
        sampling_twenty.setEnabled(false);
        sampling_forty.setEnabled(false);
        sampling_fifty.setEnabled(false);
        sampling_hundred.setEnabled(false);
        sampling_max.setEnabled(false);
        modality_real_time.setEnabled(false);
        modality_not_real_time.setEnabled(false);
        recognition_peak.setEnabled(false);
        recognition_intersection.setEnabled(false);
        filter_bagilevi.setEnabled(false);
        filter_low_pass.setEnabled(false);
        no_filter.setEnabled(false);
        filter_rotation.setEnabled(false);
        cutoff_two.setEnabled(false);
        cutoff_three.setEnabled(false);
        cutoff_ten.setEnabled(false);
        cutoff_divided_fifty.setEnabled(false);
        cutoff_frequency_layout.setEnabled(false);
        layout_sampling_rate.setEnabled(false);
        text_sampling_rate.setEnabled(false);
        first_view.setEnabled(false);
    }

}
