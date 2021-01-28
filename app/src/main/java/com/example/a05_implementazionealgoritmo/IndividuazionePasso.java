package com.example.a05_implementazionealgoritmo;

import android.util.Log;

import java.math.BigDecimal;
import java.math.MathContext;

public class IndividuazionePasso {

    private Configurazione configurazione;
    private Long scarto_tra_passi;

    public IndividuazionePasso(Configurazione configurazione) {
        this.configurazione=configurazione;
        differenza_precedente=BigDecimal.valueOf(0);
        previous_macth =-1;
        threshold_sum=configurazione.getIndividuazione_threshold();
        event_sum=1;
        scarto_tra_passi= Long.valueOf(200); // 0,2 seconds
    }

    /*
    nei documenti viene identificato come i passi avvengano ad almeno una distanza di 0.2 secondi di distanza
    10.5 trovato nelle implemetazioni online per i valori non lineari
    nella maggior parte degli studi (e anche nei graafici che mi si son creati) viene evidenziata uno scarto di valore di almeno 5 nei passi individuati
    questo valore viene aggiornato dinamicamente seguendo il flusso dei valori dei passi che vengono individuati
    */
    private BigDecimal threshold_app, threshold_sum;
    private Integer event_sum;
    public Boolean individuazionePassoRealTime_SogliaPicco_DifferenzaPiccoValle(){
        if(configurazione.getIstante_seconda_parte_ultimo_passo()-configurazione.getIstante_ultimo_passo_individuato() > scarto_tra_passi) {
            if (configurazione.getUltimo_massimo_locale_accelerometro_valore().compareTo(BigDecimal.valueOf(10.5)) == 1) {
                threshold_app = configurazione.getIndividuazione_threshold().multiply(BigDecimal.valueOf(3), MathContext.DECIMAL32).divide(BigDecimal.valueOf(5), MathContext.DECIMAL32);
                if (configurazione.get_differenza_ultimo_massimo_minimo_locale_accelerometro_valore().compareTo(threshold_app) == 1) {
                    threshold_sum = threshold_sum.add(configurazione.get_differenza_ultimo_massimo_minimo_locale_accelerometro_valore());
                    event_sum++;
                    configurazione.setIndividuazione_threshold(threshold_sum.divide(BigDecimal.valueOf(event_sum), MathContext.DECIMAL32));
                    configurazione.setIstante_ultimo_passo_individuato(configurazione.getIstante_seconda_parte_ultimo_passo());
                    return true; //passo individuato
                } else {
                    threshold_sum = threshold_sum.add(configurazione.getIndividuazione_threshold());
                    event_sum++;
                    configurazione.setIndividuazione_threshold(threshold_sum.divide(BigDecimal.valueOf(event_sum), MathContext.DECIMAL32));
                }
            }
        }
        return false;
    }

    public Boolean individuazionePassoRealTime_IstanteIntersezione(){
        if( configurazione.getIstante_prima_parte_ultimo_passo() < configurazione.getIstante_seconda_parte_ultimo_passo() ) {
            if ((configurazione.getIstante_prima_parte_ultimo_passo() < configurazione.getUltima_intersezione_asse_ascisse_istante()) && (configurazione.getIstante_seconda_parte_ultimo_passo() > configurazione.getUltima_intersezione_asse_ascisse_istante()))
                return true; //passo individuato
        }
        else { //fix per bagilevi
            if ((configurazione.getIstante_seconda_parte_ultimo_passo() < configurazione.getUltima_intersezione_asse_ascisse_istante()) && (configurazione.getIstante_prima_parte_ultimo_passo() > configurazione.getUltima_intersezione_asse_ascisse_istante()))
                return true; //passo individuato
        }
        return false;
    }

    private BigDecimal differenza, differenza_precedente;
    private Boolean step_detected;
    private Boolean isAlmostAsLargeAsPrevious, isPreviousLargeEnough, isNotContra, isStepDifferenceEnough;
    private Integer previous_macth;
    public Boolean individuazionePassoRealTime_Bagilevi(Integer match){

        step_detected = false;
        if(configurazione.getIndividuazione_continue_bagilevi()) {
            differenza = configurazione.get_differenza_ultimo_massimo_minimo_locale_accelerometro_valore();

            if (differenza.compareTo(BigDecimal.valueOf(10)) == 1) {

                isAlmostAsLargeAsPrevious = differenza.compareTo(differenza_precedente.multiply(BigDecimal.valueOf(2).divide(BigDecimal.valueOf(3), MathContext.DECIMAL32), MathContext.DECIMAL32)) == 1;
                isPreviousLargeEnough = differenza_precedente.compareTo(differenza.divide(BigDecimal.valueOf(3), MathContext.DECIMAL32)) == 1;
                isNotContra = (previous_macth != 1 - match);

                if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                    previous_macth = match;
                    step_detected = true; //passo individuato
                } else {
                    previous_macth = -1;
                }
            }
            differenza_precedente = differenza;
        }
        
        return step_detected;
    }
}
