package com.example.a05_implementazionealgoritmo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;
import java.util.ArrayList;

public class CompareConfigurations extends AppCompatActivity {
    private Button add_configuration, start_comparison;
    private Integer number_of_configurations_selected;
    private TextView number_selected;
    private ArrayList<Configurazione> configurations;
    private Configurazione configurazione_app;
    private FrameLayout frame_layout;
    private EnterSettingsFragment enter_settings_fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_configurations);

        add_configuration=findViewById(R.id.add_configuration);
        start_comparison=findViewById(R.id.start_comparison);
        number_selected=findViewById(R.id.number_selected);
        frame_layout=findViewById(R.id.frame_layout);

        number_of_configurations_selected=0;
        configurations=new ArrayList<>();

        configurazione_app=new Configurazione();
        enter_settings_fragment=new EnterSettingsFragment(configurazione_app, false);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, enter_settings_fragment).commit();

        add_configuration.setOnClickListener(v -> {
            start_comparison.setEnabled(true);
            number_of_configurations_selected++;
            if(number_of_configurations_selected<7) {
                number_selected.setText("(" + String.valueOf(number_of_configurations_selected) + ")");
                configurations.add(configurazione_app);
                Toast.makeText(this, getResources().getString(R.string.configuration_saved), Toast.LENGTH_SHORT).show();
                if(number_of_configurations_selected<6) {
                    configurazione_app = new Configurazione();
                    enter_settings_fragment=new EnterSettingsFragment(configurazione_app, false);
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame_layout, enter_settings_fragment).commit();
                }
                if (number_of_configurations_selected == 6){
                    frame_layout.setAlpha(0.5f);
                    enter_settings_fragment.disableAllViews();
                    add_configuration.setEnabled(false);
                }
            }
        });
        
        start_comparison.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SelectTest.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION).putExtra("configurations", configurations));
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }
}
