package com.example.a05_implementazionealgoritmo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class ConfigurationsComparison extends AppCompatActivity{
    private ArrayList<Configurazione> configurations;
    private Configurazione my_local_configuration;
    private EntityTest test_app;
    private String test_id;
    private ArrayList<Configurazione> my_dataset;
    private ArrayList<Integer> my_color_dataset;
    private ArrayList<Integer> my_passi_dataset;
    private AdapterForConfigurationsCard my_adapter;
    private Integer counter;
    private LineDataSet line;
    private LineData set_of_lines;
    private JSONObject json_object;
    private Iterator<String> my_iterator;
    private String key_app;

    private Integer[] my_colors={Color.BLUE, Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.BLACK};
    private ArrayList<LineDataSet> my_lines;
    private Boolean passo_inidividuato, accelerometer_event, first_signal;
    private Sensore accelerometro, accelerometro_ascisse, magnetometro;
    private Filtri filtri;
    private Calcoli calcoli;
    private RiconoscimentoValoriChiave riconoscimento_valori_chiave;
    private IndividuazionePasso individuazione_passo;
    private Integer frequenza_taglio_selected, frequenza_taglio_value, frequenza_campionamento_value, numbero_of_signals, first_second, current_second;
    private BigDecimal alpha;
    private BigDecimal last_acceleration_magnitude;
    private Integer numero_passi_individuati;
    private Calendar date;

    private LineChart line_chart;
    private Button start_new_comparison, select_another_test;
    private RecyclerView recycler_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configiurations_comparison);
        configurations = (ArrayList<Configurazione>) getIntent().getSerializableExtra("configurations");
        test_id = (String) getIntent().getSerializableExtra("test");
        try {
            test_app = new AsyncEntryTestGetFromId().execute(Integer.parseInt(test_id)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        line_chart=findViewById(R.id.line_chart);
        recycler_view=findViewById(R.id.recycler_view);
        start_new_comparison=findViewById(R.id.start_new_comparison);
        select_another_test=findViewById(R.id.select_another_test);
        start_new_comparison.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CompareConfigurations.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)));
        select_another_test.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SelectTest.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION).putExtra("configurations", configurations)));

        //inserimento linee nel grafico
        line_chart.getDescription().setEnabled(false);
        line_chart.getXAxis().setDrawLabels(false);
        line = new LineDataSet(null, "Magnitude of Acceleration");
        line.setColor(Color.RED);
        line.setDrawCircles(false);
        line.setDrawValues(false);
        set_of_lines=new LineData();
        set_of_lines.addDataSet(line);
        line_chart.setData(set_of_lines);
        counter=0;
        try {
            json_object=new JSONObject(test_app.test_values);
            my_iterator = json_object.keys();
            while(my_iterator.hasNext()) {
                key_app=my_iterator.next();
                if(json_object.getJSONObject(key_app).has("acceleration_magnitude")) {
                    set_of_lines = line_chart.getData();
                    set_of_lines.addEntry(new Entry(counter, Float.valueOf(  json_object.getJSONObject(key_app).getString("acceleration_magnitude"))), 0);
                    counter++;
                }
            }
            set_of_lines.notifyDataChanged();
            line_chart.notifyDataSetChanged();
            line_chart.setVisibleXRangeMaximum(MainActivity.NUMBER_OF_DOTS_IN_GRAPH);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        my_dataset =new ArrayList<Configurazione>();
        my_color_dataset = new ArrayList<Integer>();
        my_passi_dataset = new ArrayList<Integer>();
        my_lines = new ArrayList<LineDataSet>();
        my_adapter = new AdapterForConfigurationsCard(my_dataset, my_color_dataset, my_passi_dataset, my_lines, this, set_of_lines, line_chart);
        recycler_view.setAdapter(my_adapter);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        for(int i=0; i<configurations.size(); i++){

            //inserimento passi delle configurazioni
            try {
                my_local_configuration=new Configurazione();
                my_local_configuration=(Configurazione) configurations.get(i).clone();
                accelerometro=new Sensore();
                accelerometro_ascisse=new Sensore();
                magnetometro=new Sensore();
                filtri=new Filtri(my_local_configuration);
                calcoli=new Calcoli(my_local_configuration);
                riconoscimento_valori_chiave =new RiconoscimentoValoriChiave(my_local_configuration);
                individuazione_passo =new IndividuazionePasso(my_local_configuration);
                my_local_configuration.setIstante_ultimo_passo_individuato(Long.valueOf(0));
                frequenza_taglio_selected=my_local_configuration.getFrequenza_di_taglio();
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
                    frequenza_campionamento_value=0;
                    numbero_of_signals=0;
                    first_signal=true;
                }
                my_iterator = json_object.keys();
                counter=0;
                numero_passi_individuati=0;
                my_lines.add(new LineDataSet(null, String.valueOf(i+1)));
                my_lines.get(i).setColor(my_colors[i]);
                my_lines.get(i).setDrawValues(false);
                my_lines.get(i).setCircleColor(my_colors[i]);
                set_of_lines = line_chart.getData();
                set_of_lines.addDataSet(my_lines.get(i));
                line_chart.setData(set_of_lines);
                while(my_iterator.hasNext()) {
                    key_app=my_iterator.next();
                    myOnSensorChanged(Long.valueOf(key_app), json_object.getJSONObject(key_app), my_local_configuration, i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            //inserimento card nella recycler_view
            my_dataset.add(my_local_configuration);
            my_color_dataset.add(my_colors[i]);
            my_passi_dataset.add(numero_passi_individuati);
        }
        my_adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    private void myOnSensorChanged(Long istante, JSONObject event_json, Configurazione configurazione, Integer index) {

        passo_inidividuato=false;

        try {
            if(event_json.has("acceleration_x")) {
                counter++;
                last_acceleration_magnitude=new BigDecimal(event_json.getString("acceleration_magnitude"));
                accelerometer_event=true;
                accelerometro.setVettore_3_componenti(new BigDecimal[] {new BigDecimal(event_json.getString("acceleration_x")), new BigDecimal(event_json.getString("acceleration_y")), new BigDecimal(event_json.getString("acceleration_z"))});
                accelerometro_ascisse.setVettore_3_componenti(new BigDecimal[] {new BigDecimal(event_json.getString("acceleration_x")), new BigDecimal(event_json.getString("acceleration_y")), new BigDecimal(event_json.getString("acceleration_z"))});
            }

            else if(event_json.has("magnetometer_x")) {
                accelerometer_event=false;
                magnetometro.setVettore_3_componenti(new BigDecimal[] {new BigDecimal(event_json.getString("magnetometer_x")), new BigDecimal(event_json.getString("magnetometer_y")), new BigDecimal(event_json.getString("magnetometer_z"))});
            }

            else if(event_json.has("gravity_x")) {
                accelerometer_event=false;
                configurazione.setAccelerazione_di_gravita(calcoli.risultante(new BigDecimal[] {new BigDecimal(event_json.getString("gravity_x")), new BigDecimal(event_json.getString("gravity_y")), new BigDecimal(event_json.getString("gravity_z"))}));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(configurazione.getReal_time()==0){ //real-time

            switch(configurazione.getFiltro()){
                case 0: //bagilevi
                    if(accelerometer_event) {
                        accelerometro.setRisultante_filtrato(filtri.filtroBagilevi(accelerometro.getVettore_3_componenti()));
                        passo_inidividuato = individuazione_passo.individuazionePassoRealTime_Bagilevi(riconoscimento_valori_chiave.riconoscimentoPuntiMassimoMinimoLocaleRealTime_Bagilevi(accelerometro.getRisultante_filtrato(), istante));
                    }
                    break;
                case 1: //passa-basso
                    if(accelerometer_event) {

                        //aggiornamento frequenza di campionamento
                        if(frequenza_taglio_selected!=3) {
                            date = Calendar.getInstance();
                            date.setTimeInMillis(istante);
                            if(first_signal) {
                                first_second = date.get(Calendar.SECOND);
                                current_second = first_second;
                                first_signal = false;
                            }
                            if(date.get(Calendar.SECOND)==first_second){
                                numbero_of_signals++;
                                frequenza_campionamento_value++;
                            }
                            else if(date.get(Calendar.SECOND)==current_second){
                                numbero_of_signals++;
                            }
                            else {
                                current_second=date.get(Calendar.SECOND);
                                frequenza_campionamento_value=numbero_of_signals;
                                numbero_of_signals=0;
                            }
                            alpha = calculateAlpha(frequenza_campionamento_value, frequenza_taglio_value);
                        }

                        accelerometro.setVettore_3_componenti_filtrato(filtri.filtroPassaBasso(accelerometro.getVettore_3_componenti(), alpha));
                        accelerometro.setRisultante_filtrato(calcoli.risultante(accelerometro.getVettore_3_componenti_filtrato()));
                        if(riconoscimento_valori_chiave.riconoscimentoPuntiMassimoMinimoLocaleRealTime(accelerometro.getRisultante_filtrato(), istante)) {
                            passo_inidividuato = individuazione_passo.individuazionePassoRealTime_SogliaPicco_DifferenzaPiccoValle();
                        }
                        else
                            passo_inidividuato = false;
                    }
                    break;
                case 2: //nessun filtro
                    if(accelerometer_event) {
                        accelerometro.setRisultante(calcoli.risultante(accelerometro.getVettore_3_componenti()));
                        if(riconoscimento_valori_chiave.riconoscimentoPuntiMassimoMinimoLocaleRealTime(accelerometro.getRisultante(), istante))
                            passo_inidividuato = individuazione_passo.individuazionePassoRealTime_SogliaPicco_DifferenzaPiccoValle();
                        else passo_inidividuato=false;
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
                numero_passi_individuati++;
                set_of_lines = line_chart.getData();
                set_of_lines.addEntry(new Entry(counter, last_acceleration_magnitude.floatValue()), index+1);
                set_of_lines.notifyDataChanged();
                line_chart.notifyDataSetChanged();
                line_chart.setVisibleXRangeMaximum(MainActivity.NUMBER_OF_DOTS_IN_GRAPH);
            }

        }

    }

    private BigDecimal calculateAlpha(Integer frequenza_campionamento, Integer frequenza_taglio_value){
        BigDecimal frequenza_campionamento_app, frequenza_taglio_value_app;

        frequenza_campionamento_app=BigDecimal.valueOf(1).divide(BigDecimal.valueOf(frequenza_campionamento), MathContext.DECIMAL32);
        frequenza_taglio_value_app=BigDecimal.valueOf(1).divide( BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(Math.PI), MathContext.DECIMAL32).multiply(BigDecimal.valueOf(frequenza_taglio_value), MathContext.DECIMAL32), MathContext.DECIMAL32);

        return frequenza_campionamento_app.divide( frequenza_campionamento_app.add(frequenza_taglio_value_app), MathContext.DECIMAL32);
    }


}
