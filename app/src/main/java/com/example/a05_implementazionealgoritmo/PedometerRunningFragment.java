package com.example.a05_implementazionealgoritmo;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Calendar;

public class PedometerRunningFragment extends Fragment implements SensorEventListener {
    private SensorManager sensor_manager;
    private Sensor accelerometer, magnetometer, gravity;
    private Boolean accelerometer_event;
    private Sensore accelerometro, accelerometro_ascisse, magnetometro;
    private Filtri filtri;
    private Calcoli calcoli;
    private RiconoscimentoValoriChiave riconoscimento_valori_chiave;
    private IndividuazionePasso individuazione_passo;
    private Configurazione configurazione;
    private Boolean passo_inidividuato;
    private Integer frequenza_campionamento_microseconds, frequenza_campionamento, frequenza_taglio_selected, frequenza_taglio_value;
    private BigDecimal alpha;
    private Integer current_second, first_current_second, number_of_sensor_events, step_count, counter;
    private Long istante;
    private LineDataSet line;
    private LineData set_of_lines; //nel mio caso ne ho solo una

    private LineChart line_chart;
    private TextView text_step_count;

    public PedometerRunningFragment() {
        this.configurazione=new Configurazione();
        accelerometro=new Sensore();
        accelerometro_ascisse=new Sensore();
        magnetometro=new Sensore();
        filtri=new Filtri(configurazione);
        calcoli=new Calcoli(configurazione);
        riconoscimento_valori_chiave =new RiconoscimentoValoriChiave(configurazione);
        individuazione_passo =new IndividuazionePasso(configurazione);
        configurazione.setIstante_ultimo_passo_individuato(Long.valueOf(0));

        current_second=Calendar.getInstance().get(Calendar.SECOND);
        first_current_second=current_second;
        step_count=0;
        counter=0;}

    public PedometerRunningFragment(Configurazione configurazione) {
        this.configurazione=configurazione;
        accelerometro=new Sensore();
        accelerometro_ascisse=new Sensore();
        magnetometro=new Sensore();
        filtri=new Filtri(configurazione);
        calcoli=new Calcoli(configurazione);
        riconoscimento_valori_chiave =new RiconoscimentoValoriChiave(configurazione);
        individuazione_passo =new IndividuazionePasso(configurazione);
        configurazione.setIstante_ultimo_passo_individuato(Long.valueOf(0));

        current_second=Calendar.getInstance().get(Calendar.SECOND);
        first_current_second=current_second;
        step_count=0;
        counter=0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pedometer_running_fragment, container, false);

        text_step_count=root.findViewById(R.id.step_count);
        line_chart =root.findViewById(R.id.line_chart);
        line_chart.getDescription().setEnabled(false);
        line_chart.getXAxis().setDrawLabels(false);

        line = new LineDataSet(null, "Magnitude of Acceleration");
        line.setColor(Color.RED);
        line.setDrawCircles(false);
        line.setDrawValues(false);
        set_of_lines=new LineData();
        set_of_lines.addDataSet(line);
        line_chart.setData(set_of_lines);

        switch (configurazione.getFrequenza_di_campionamento()){
            case 0: // 20Hz = 50000 microSeconds
                frequenza_campionamento=20;
                frequenza_campionamento_microseconds =50000;
                break;
            case 1: // 40Hz = 25000 microSeconds
                frequenza_campionamento=40;
                frequenza_campionamento_microseconds =25000;
                break;
            case 2: // 50Hz = 20000 microSeconds
                frequenza_campionamento=50;
                frequenza_campionamento_microseconds =20000;
                break;
            case 3: // 100Hz = 10000 microSeconds
                frequenza_campionamento=100;
                frequenza_campionamento_microseconds =10000;
                break;
            case 4:
                frequenza_campionamento=250;
                frequenza_campionamento_microseconds =SensorManager.SENSOR_DELAY_FASTEST;
                break;
        }
        number_of_sensor_events=frequenza_campionamento;

        frequenza_taglio_selected=configurazione.getFrequenza_di_taglio();
        if(frequenza_taglio_selected==3) // alpha=0.1
            alpha = BigDecimal.valueOf(0.1);
        else {
            switch (frequenza_taglio_selected) {
                case 0: // frequenza_taglio=2
                    frequenza_taglio_value=2;
                    break;
                case 1: // frequenza_taglio=3
                    frequenza_taglio_value=3;
                    break;
                case 2: // frequenza_taglio=10
                    frequenza_taglio_value=10;
                    break;
            }
            alpha = calculateAlpha(frequenza_campionamento, frequenza_taglio_value);
        }

        sensor_manager= (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer=sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer=sensor_manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gravity=sensor_manager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        sensor_manager.registerListener(this, accelerometer, frequenza_campionamento_microseconds);
        sensor_manager.registerListener(this, magnetometer, frequenza_campionamento_microseconds);
        sensor_manager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        istante=System.currentTimeMillis();

        passo_inidividuato=false;

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelerometer_event=true;
            accelerometro.setVettore_3_componenti(new BigDecimal[] {BigDecimal.valueOf(event.values[0]), BigDecimal.valueOf(event.values[1]), BigDecimal.valueOf(event.values[2])});
            accelerometro_ascisse.setVettore_3_componenti(new BigDecimal[] {BigDecimal.valueOf(event.values[0]), BigDecimal.valueOf(event.values[1]), BigDecimal.valueOf(event.values[2])});

            if(Calendar.getInstance().get(Calendar.SECOND)!=first_current_second) {
                if (Calendar.getInstance().get(Calendar.SECOND) == current_second)
                    number_of_sensor_events++;
                else {
                    frequenza_campionamento = number_of_sensor_events;
                    if(frequenza_taglio_selected!=3){
                        switch (frequenza_taglio_selected) {
                            case 0: // frequenza_taglio=2
                                frequenza_taglio_value=2;
                                break;
                            case 1: // frequenza_taglio=3
                                frequenza_taglio_value=3;
                                break;
                            case 2: // frequenza_taglio=10
                                frequenza_taglio_value=10;
                                break;
                        }
                        alpha = calculateAlpha(frequenza_campionamento, frequenza_taglio_value);
                    }
                    current_second = Calendar.getInstance().get(Calendar.SECOND);
                    number_of_sensor_events = 0;
                }
            }
        }

        else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            accelerometer_event=false;
            magnetometro.setVettore_3_componenti(new BigDecimal[] {BigDecimal.valueOf(event.values[0]), BigDecimal.valueOf(event.values[1]), BigDecimal.valueOf(event.values[2])});
        }

        else if(event.sensor.getType() == Sensor.TYPE_GRAVITY){
            accelerometer_event=false;
            configurazione.setAccelerazione_di_gravita(calcoli.risultante(new BigDecimal[] {BigDecimal.valueOf(event.values[0]), BigDecimal.valueOf(event.values[1]), BigDecimal.valueOf(event.values[2])}));
        }

        if(configurazione.getReal_time()==0){ //real-time

            switch(configurazione.getFiltro()){
                case 0: //bagilevi
                    if(accelerometer_event) {
                        accelerometro.setRisultante_filtrato(filtri.filtroBagilevi(accelerometro.getVettore_3_componenti()));
                        passo_inidividuato = individuazione_passo.individuazionePassoRealTime_Bagilevi(riconoscimento_valori_chiave.riconoscimentoPuntiMassimoMinimoLocaleRealTime_Bagilevi(accelerometro.getRisultante_filtrato(), istante));

                        line.setLabel("Magnitude of Acceleration - Bagilevi Algorithm");
                        set_of_lines = line_chart.getData();
                        set_of_lines.addEntry(new Entry(counter, accelerometro.getRisultante_filtrato().floatValue()), 0);
                        set_of_lines.notifyDataChanged();
                        line_chart.notifyDataSetChanged();
                        line_chart.setVisibleXRangeMaximum(MainActivity.NUMBER_OF_DOTS_IN_GRAPH);
                        line_chart.moveViewToX(set_of_lines.getEntryCount());
                        counter++;
                    }
                    break;
                case 1: //passa-basso
                    if(accelerometer_event) {
                        accelerometro.setVettore_3_componenti_filtrato(filtri.filtroPassaBasso(accelerometro.getVettore_3_componenti(), alpha));
                        accelerometro.setRisultante_filtrato(calcoli.risultante(accelerometro.getVettore_3_componenti_filtrato()));
                        if(riconoscimento_valori_chiave.riconoscimentoPuntiMassimoMinimoLocaleRealTime(accelerometro.getRisultante_filtrato(), istante))
                            passo_inidividuato = individuazione_passo.individuazionePassoRealTime_SogliaPicco_DifferenzaPiccoValle();
                        else
                            passo_inidividuato = false;

                        line.setLabel("Magnitude of Acceleration - Low-Pass Filter");
                        set_of_lines = line_chart.getData();
                        set_of_lines.addEntry(new Entry(counter, accelerometro.getRisultante_filtrato().floatValue()), 0);
                        set_of_lines.notifyDataChanged();
                        line_chart.notifyDataSetChanged();
                        line_chart.setVisibleXRangeMaximum(MainActivity.NUMBER_OF_DOTS_IN_GRAPH);
                        line_chart.moveViewToX(set_of_lines.getEntryCount());
                        counter++;
                    }
                    break;
                case 2: //nessun filtro
                    if(accelerometer_event) {
                        accelerometro.setRisultante(calcoli.risultante(accelerometro.getVettore_3_componenti()));
                        if(riconoscimento_valori_chiave.riconoscimentoPuntiMassimoMinimoLocaleRealTime(accelerometro.getRisultante(), istante))
                            passo_inidividuato = individuazione_passo.individuazionePassoRealTime_SogliaPicco_DifferenzaPiccoValle();
                        else passo_inidividuato=false;

                        line.setLabel("Magnitude of Acceleration - No Filter");
                        set_of_lines = line_chart.getData();
                        set_of_lines.addEntry(new Entry(counter, accelerometro.getRisultante().floatValue()), 0);
                        set_of_lines.notifyDataChanged();
                        line_chart.notifyDataSetChanged();
                        line_chart.setVisibleXRangeMaximum(MainActivity.NUMBER_OF_DOTS_IN_GRAPH);
                        line_chart.moveViewToX(set_of_lines.getEntryCount());
                        counter++;
                    }
                    break;
                case 3: //matrice_rotazione
                    if(accelerometro.getIstanziato() && magnetometro.getIstanziato()) {
                        calcoli.matriceRotazione(accelerometro.getVettore_3_componenti(), magnetometro.getVettore_3_componenti());
                        accelerometro.setVettore_3_componenti_sistema_fisso(calcoli.accelerazioneSistemaFisso(accelerometro.getVettore_3_componenti()));
                        if(riconoscimento_valori_chiave.riconoscimentoPuntiMassimoMinimoLocaleRealTime(accelerometro.getVettore_3_componenti_sistema_fisso()[2], istante))
                            passo_inidividuato = individuazione_passo.individuazionePassoRealTime_SogliaPicco_DifferenzaPiccoValle();
                        else passo_inidividuato=false;
                    }
                    if(accelerometer_event) {
                        line.setLabel("Z Component of Fixed Acceleration - Rotation Matrix");
                        set_of_lines = line_chart.getData();
                        set_of_lines.addEntry(new Entry(counter, accelerometro.getVettore_3_componenti_sistema_fisso()[2].floatValue()), 0);
                        set_of_lines.notifyDataChanged();
                        line_chart.notifyDataSetChanged();
                        line_chart.setVisibleXRangeMaximum(MainActivity.NUMBER_OF_DOTS_IN_GRAPH);
                        line_chart.moveViewToX(set_of_lines.getEntryCount());
                        counter++;
                    }
                    break;
            }

            if(configurazione.getAlgoritmo_di_riconoscimento()==1) { //algoritmo picchi + correzione algoritmo intersezioni
                if(accelerometer_event) {
                    accelerometro_ascisse.setRisultante(calcoli.risultante(accelerometro_ascisse.getVettore_3_componenti()));
                    accelerometro_ascisse.setRisultante_lineare(calcoli.accelerazioneLineare(accelerometro_ascisse.getRisultante()));
                    riconoscimento_valori_chiave.riconoscimentoPuntiIntersezioneAsseAscisseRealTime(accelerometro_ascisse.getRisultante_lineare(), istante);
                    passo_inidividuato = passo_inidividuato && individuazione_passo.individuazionePassoRealTime_IstanteIntersezione();
                }
                else
                    passo_inidividuato=false;
            }

            if(passo_inidividuato){
                stepDetected();
            }

        }

    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private BigDecimal calculateAlpha(Integer frequenza_campionamento, Integer frequenza_taglio_value){
        BigDecimal frequenza_campionamento_app, frequenza_taglio_value_app;

        frequenza_campionamento_app=BigDecimal.valueOf(1).divide(BigDecimal.valueOf(frequenza_campionamento), MathContext.DECIMAL32);
        frequenza_taglio_value_app=BigDecimal.valueOf(1).divide( BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(Math.PI), MathContext.DECIMAL32).multiply(BigDecimal.valueOf(frequenza_taglio_value), MathContext.DECIMAL32), MathContext.DECIMAL32);

        return frequenza_campionamento_app.divide( frequenza_campionamento_app.add(frequenza_taglio_value_app), MathContext.DECIMAL32);
    }

    private void stepDetected(){
        step_count++;
        text_step_count.setText(String.valueOf(step_count));
    }


}
