package com.example.a05_implementazionealgoritmo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class NewTest extends AppCompatActivity implements SensorEventListener {
    private LineDataSet line;
    private LineData set_of_lines; //nel mio caso ne ho solo una
    private HashMap<String, JSONObject> new_test;
    private SensorManager sensor_manager;
    private Sensor accelerometer, magnetometer, gravity;
    private Integer counter;
    private Long istante;
    private Calcoli calcoli;
    private JSONObject json_object_app;
    private BigDecimal risultante_accelerometro;
    private AlertDialog.Builder dialogBuilder;
    private View dialogView;
    private EditText number_of_steps, additional_notes;
    private Button save_new_test;
    private AlertDialog alertDialog;

    private Button new_test_button;
    private LineChart line_chart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_test);

        new_test_button =findViewById(R.id.new_test_button);
        line_chart = findViewById(R.id.line_chart);
        line_chart.getDescription().setEnabled(false);
        line_chart.getXAxis().setDrawLabels(false);

        line = new LineDataSet(null, "Magnitude of Acceleration");
        line.setColor(Color.RED);
        line.setDrawCircles(false);
        line.setDrawValues(false);
        set_of_lines=new LineData();
        set_of_lines.addDataSet(line);
        line_chart.setData(set_of_lines);

        set_of_lines = line_chart.getData();
        set_of_lines.addEntry(new Entry(0, 0), 0);
        set_of_lines.notifyDataChanged();
        line_chart.notifyDataSetChanged();
        line_chart.setVisibleXRangeMaximum(MainActivity.NUMBER_OF_DOTS_IN_GRAPH);
        line_chart.moveViewToX(set_of_lines.getEntryCount());

        calcoli=new Calcoli();
        counter=0;
        new_test = new LinkedHashMap<>();
        new_test_button.setText(R.string.start_new_test);
        new_test_button.setOnClickListener(v -> { //started recording new test
            new_test_button.setText(R.string.stop_new_test);
            sensor_manager= (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
            accelerometer=sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetometer=sensor_manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            gravity=sensor_manager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            sensor_manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
            sensor_manager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
            sensor_manager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_FASTEST);

            new_test_button.setOnClickListener(v1 -> { //stopped recording new test
                new_test_button.setText(R.string.save_new_test);
                sensor_manager.unregisterListener(this);

                dialogBuilder = new AlertDialog.Builder(this);
                dialogView = getLayoutInflater().inflate(R.layout.number_of_steps, null);
                dialogBuilder.setView(dialogView);
                number_of_steps = dialogView.findViewById(R.id.number_of_steps);
                additional_notes = dialogView.findViewById(R.id.additional_notes);
                save_new_test = dialogView.findViewById(R.id.save_new_test);
                save_new_test.setOnClickListener(v2 -> {
                    if(!String.valueOf(number_of_steps.getText()).equals("")) {
                        new AsyncEntryTestInsertNew(this).execute(new JSONObject(new_test).toString(), String.valueOf(number_of_steps.getText()), String.valueOf(additional_notes.getText()));
                        Toast.makeText(this, getResources().getString(R.string.new_test_saved), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }
                    else Toast.makeText(this, getResources().getString(R.string.number_steps_error), Toast.LENGTH_SHORT).show();
                });
                alertDialog = dialogBuilder.create();
                alertDialog.show();
            });
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        istante = System.currentTimeMillis();
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            risultante_accelerometro=calcoli.risultante(new BigDecimal[]{BigDecimal.valueOf(event.values[0]), BigDecimal.valueOf(event.values[1]), BigDecimal.valueOf(event.values[2])});
            set_of_lines = line_chart.getData();
            set_of_lines.addEntry(new Entry(counter, risultante_accelerometro.floatValue()), 0);
            json_object_app = new JSONObject();
            try {
                json_object_app.put("acceleration_x", String.valueOf(BigDecimal.valueOf(event.values[0])));
                json_object_app.put("acceleration_y", String.valueOf(BigDecimal.valueOf(event.values[1])));
                json_object_app.put("acceleration_z", String.valueOf(BigDecimal.valueOf(event.values[2])));
                json_object_app.put("acceleration_magnitude", String.valueOf(risultante_accelerometro));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new_test.put(String.valueOf(istante), json_object_app);
            set_of_lines.notifyDataChanged();
            line_chart.notifyDataSetChanged();
            line_chart.setVisibleXRangeMaximum(MainActivity.NUMBER_OF_DOTS_IN_GRAPH);
            line_chart.moveViewToX(set_of_lines.getEntryCount());
            counter++;
        }
        else if(event.sensor.getType()== Sensor.TYPE_MAGNETIC_FIELD){
            json_object_app = new JSONObject();
            try {
                json_object_app.put("magnetometer_x", String.valueOf(BigDecimal.valueOf(event.values[0])));
                json_object_app.put("magnetometer_y", String.valueOf(BigDecimal.valueOf(event.values[1])));
                json_object_app.put("magnetometer_z", String.valueOf(BigDecimal.valueOf(event.values[2])));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new_test.put(String.valueOf(istante), json_object_app);
        }
        else if(event.sensor.getType()== Sensor.TYPE_GRAVITY){
            json_object_app = new JSONObject();
            try {
                json_object_app.put("gravity_x", String.valueOf(BigDecimal.valueOf(event.values[0])));
                json_object_app.put("gravity_y", String.valueOf(BigDecimal.valueOf(event.values[1])));
                json_object_app.put("gravity_z", String.valueOf(BigDecimal.valueOf(event.values[2])));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new_test.put(String.valueOf(istante), json_object_app);
        }
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
