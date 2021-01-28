package com.example.a05_implementazionealgoritmo;

import android.util.Log;

import java.math.BigDecimal;

public class RiconoscimentoValoriChiave {

    private Configurazione configurazione;
    private BigDecimal app= BigDecimal.valueOf(1000);

    public RiconoscimentoValoriChiave(Configurazione configurazione) {
        this.configurazione=configurazione;
        first_value=true;
        previous_direction=0;
        risultante_accelerometro_precedente=BigDecimal.valueOf(0);
        previous_value= BigDecimal.valueOf(0);
        previous_istante= Long.valueOf(0);
        peak_detected=false;
        valley_detected=false;
        peak_detected_value=BigDecimal.valueOf(0);
    }

    private BigDecimal risultante_accelerometro_precedente;
    private Integer direction, previous_direction;
    public Integer riconoscimentoPuntiMassimoMinimoLocaleRealTime_Bagilevi(BigDecimal risultante_accelerometro, Long istante){

        direction = risultante_accelerometro.compareTo(risultante_accelerometro_precedente);
        if (direction == -previous_direction) { // Graph direction changed
            configurazione.setIndividuazione_continue_bagilevi(true);
            if(direction>0){
                configurazione.setUltimo_minimo_locale_accelerometro_valore(risultante_accelerometro);
                configurazione.setIstante_prima_parte_ultimo_passo(istante);
            }
            else{
                configurazione.setUltimo_massimo_locale_accelerometro_valore(risultante_accelerometro);
                configurazione.setIstante_seconda_parte_ultimo_passo(istante);
            }
        }
        else configurazione.setIndividuazione_continue_bagilevi(false);
        previous_direction = direction;
        risultante_accelerometro_precedente = risultante_accelerometro;

        if (direction>0) return 0;
        else return 1;
    }

    private Boolean peak_detected, valley_detected;
    private BigDecimal previous_value, peak_detected_value;
    private Long previous_istante;
    //controlla se avviene un picco (registra i valori finche non registra quello più alto)
    //se dopo avvengono altre curve più alte, sempre rimandendo sopra la soglia, allora aggiorna il picco
    //quando, dopo aver trovato un picco, scende sotto la soglia registra i valori fino al più basso (la valle significativa di un passo è quasi sempre la prima)
    public Boolean riconoscimentoPuntiMassimoMinimoLocaleRealTime(BigDecimal risultante_accelerometro, Long istante){

        if(risultante_accelerometro.compareTo(previous_value)<1) {
            valley_detected=false;
            if(!peak_detected || previous_value.compareTo(peak_detected_value)==1) {
                peak_detected_value=previous_value;
                configurazione.setUltimo_massimo_locale_accelerometro_valore(previous_value);
                configurazione.setIstante_prima_parte_ultimo_passo(previous_istante);
                peak_detected = true;
            }
        }
        else if(peak_detected && risultante_accelerometro.compareTo(previous_value)>-1) {
            if(!valley_detected) {
                peak_detected_value=BigDecimal.valueOf(0);
                configurazione.setUltimo_minimo_locale_accelerometro_valore(previous_value);
                configurazione.setIstante_seconda_parte_ultimo_passo(previous_istante);
                valley_detected = true;
            }
        }

        previous_value = risultante_accelerometro;
        previous_istante = istante;

        if(valley_detected){
            peak_detected=false;
            valley_detected=false;
            return true;
        }
        else return false;
    }

    private Boolean first_value, previously_positive_value;
    public void riconoscimentoPuntiIntersezioneAsseAscisseRealTime(BigDecimal risultante_accelerometro, Long istante){

        if(first_value){
            if(risultante_accelerometro.compareTo(BigDecimal.valueOf(0))>=0)
                previously_positive_value =true;
            else
                previously_positive_value =false;
            first_value=false;
        }
        else{
            if (previously_positive_value && risultante_accelerometro.compareTo(BigDecimal.valueOf(0))==-1){
                previously_positive_value =false;
                configurazione.setUltima_intersezione_asse_ascisse_istante(istante);
            }
            else if (!previously_positive_value && risultante_accelerometro.compareTo(BigDecimal.valueOf(0))>=0){
                previously_positive_value =true;
                configurazione.setUltima_intersezione_asse_ascisse_istante(istante);
            }
        }

        return;
    }
}
