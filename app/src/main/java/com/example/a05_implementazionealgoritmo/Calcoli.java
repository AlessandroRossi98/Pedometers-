package com.example.a05_implementazionealgoritmo;

import android.hardware.SensorManager;

import java.math.BigDecimal;
import java.math.MathContext;

public class Calcoli {

    private Configurazione configurazione;

    public Calcoli(Configurazione configurazione) {
        this.configurazione=configurazione;}

    public Calcoli() {}

    public BigDecimal risultante(BigDecimal[] vettore_3_componenti){
        return BigDecimal.valueOf(Math.sqrt(Math.pow(Math.sqrt(Math.pow(vettore_3_componenti[0].doubleValue(), 2) + Math.pow(vettore_3_componenti[1].doubleValue(), 2)), 2) + Math.pow(vettore_3_componenti[2].doubleValue(), 2)));
    }

    public BigDecimal accelerazioneLineare(BigDecimal risultante){
        return risultante.subtract(configurazione.getAccelerazione_di_gravita());
    }

    float R[] = new float[16];
    float gravity[] = new float[3];
    float geomagnetic[] = new float[3];
    public void matriceRotazione(BigDecimal[] vettore_3_componenti_accelerometro, BigDecimal[] vettore_3_componenti_magnetometro){
        gravity[0]=vettore_3_componenti_accelerometro[0].floatValue();
        gravity[1]=vettore_3_componenti_accelerometro[1].floatValue();
        gravity[2]=vettore_3_componenti_accelerometro[2].floatValue();
        geomagnetic[0]=vettore_3_componenti_magnetometro[0].floatValue();
        geomagnetic[1]=vettore_3_componenti_magnetometro[1].floatValue();
        geomagnetic[2]=vettore_3_componenti_magnetometro[2].floatValue();

        SensorManager.getRotationMatrix(R, null, gravity, geomagnetic);
        configurazione.setMatrice_di_rotazione(new BigDecimal[][]{{BigDecimal.valueOf(R[0]), BigDecimal.valueOf(R[1]), BigDecimal.valueOf(R[2])}, {BigDecimal.valueOf(R[4]), BigDecimal.valueOf(R[5]), BigDecimal.valueOf(R[6])}, {BigDecimal.valueOf(R[8]), BigDecimal.valueOf(R[9]), BigDecimal.valueOf(R[10])}});
    }

    Integer i, j;
    BigDecimal sum_app;
    public BigDecimal[] accelerazioneSistemaFisso(BigDecimal[] vettore_3_componenti_accelerometro){
        BigDecimal[] vettore_3_componenti_accelerometro_sistema_fisso=new BigDecimal[] {BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0)};

        for(i=0; i<3; i++){
            sum_app= BigDecimal.valueOf(0);
            for(j=0; j<3; j++){
                sum_app=sum_app.add(configurazione.getMatrice_di_rotazione()[i][j].multiply(vettore_3_componenti_accelerometro[j], MathContext.DECIMAL32));
            }
            vettore_3_componenti_accelerometro_sistema_fisso[i]=sum_app;
        }

        return vettore_3_componenti_accelerometro_sistema_fisso;
    }
}
