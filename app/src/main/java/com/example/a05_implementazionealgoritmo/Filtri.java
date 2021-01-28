package com.example.a05_implementazionealgoritmo;

import android.hardware.SensorManager;

import java.math.BigDecimal;
import java.math.MathContext;

public class Filtri {

    public Filtri(Configurazione configurazione) {
        vettore_3_componenti_filtrato=new BigDecimal[] {BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0)};
    }

    private BigDecimal[] vettore_3_componenti_filtrato;
    public BigDecimal[] filtroPassaBasso(BigDecimal[] vettore_3_componenti, BigDecimal alpha){

        for(int i=0; i<3; i++){
            vettore_3_componenti_filtrato[i] = vettore_3_componenti_filtrato[i].add( alpha.multiply( vettore_3_componenti[i].subtract(vettore_3_componenti_filtrato[i]), MathContext.DECIMAL32 ) );
        }

        return vettore_3_componenti_filtrato;
    }

    BigDecimal sum_of_values;
    public BigDecimal filtroBagilevi(BigDecimal[] vettore_3_componenti){

        sum_of_values = BigDecimal.valueOf(0);
        for (int i=0 ; i<3 ; i++) {
            sum_of_values=sum_of_values.add( BigDecimal.valueOf(4).multiply( BigDecimal.valueOf(SensorManager.MAGNETIC_FIELD_EARTH_MAX).subtract(vettore_3_componenti[i]), MathContext.DECIMAL32 ));
        }

        return sum_of_values.divide(BigDecimal.valueOf(3), MathContext.DECIMAL32);
    }
}
