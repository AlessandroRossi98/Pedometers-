package com.example.a05_implementazionealgoritmo;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LiveTesting extends AppCompatActivity {

    private Configurazione configurazione;
    private PedometerRunningFragment pedometer_running_fragment;

    private Button new_pedometer, start_pedometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_testing);

        new_pedometer=findViewById(R.id.new_pedometer);
        start_pedometer=findViewById(R.id.start_pedometer);

        new_pedometer.setOnClickListener(v -> {
            configurazione=new Configurazione();
            ((SensorManager) getSystemService(Context.SENSOR_SERVICE)).unregisterListener(pedometer_running_fragment);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new EnterSettingsFragment(configurazione)).commit();
            new_pedometer.setVisibility(View.GONE);
            start_pedometer.setVisibility(View.VISIBLE);
        });

        start_pedometer.setOnClickListener(v -> {
            pedometer_running_fragment=new PedometerRunningFragment(configurazione);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, pedometer_running_fragment).commit();
            new_pedometer.setVisibility(View.VISIBLE);
            start_pedometer.setVisibility(View.GONE);
        });

        new_pedometer.callOnClick();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }
}
