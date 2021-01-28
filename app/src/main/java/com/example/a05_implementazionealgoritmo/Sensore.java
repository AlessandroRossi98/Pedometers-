package com.example.a05_implementazionealgoritmo;

import java.math.BigDecimal;

public class Sensore {

    private BigDecimal[] vettore_3_componenti;
    private BigDecimal[] vettore_3_componenti_filtrato;
    private BigDecimal[] vettore_3_componenti_sistema_fisso;
    private BigDecimal risultante;
    private BigDecimal risultante_filtrato;
    private BigDecimal risultante_lineare;
    private Boolean istanziato=false;

    public Sensore() {
        vettore_3_componenti = new BigDecimal[]{BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0)};
        vettore_3_componenti_filtrato = new BigDecimal[]{BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0)};
        vettore_3_componenti_sistema_fisso = new BigDecimal[]{BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0)};
        risultante=BigDecimal.valueOf(0);
        risultante_filtrato =BigDecimal.valueOf(0);
        risultante_lineare =BigDecimal.valueOf(0);
    }

    public BigDecimal[] getVettore_3_componenti() {
        return vettore_3_componenti;
    }

    public void setVettore_3_componenti(BigDecimal[] vettore_3_componenti) {
        setIstanziato(true);
        this.vettore_3_componenti = vettore_3_componenti;
    }

    public BigDecimal[] getVettore_3_componenti_filtrato() {
        return vettore_3_componenti_filtrato;
    }

    public void setVettore_3_componenti_filtrato(BigDecimal[] vettore_3_componenti_filtrato) {
        this.vettore_3_componenti_filtrato = vettore_3_componenti_filtrato;
    }

    public BigDecimal[] getVettore_3_componenti_sistema_fisso() {
        return vettore_3_componenti_sistema_fisso;
    }

    public void setVettore_3_componenti_sistema_fisso(BigDecimal[] vettore_3_componenti_sistema_fisso) {
        this.vettore_3_componenti_sistema_fisso = vettore_3_componenti_sistema_fisso;
    }

    public BigDecimal getRisultante() {
        return risultante;
    }

    public void setRisultante(BigDecimal risultante) {
        this.risultante = risultante;
    }

    public BigDecimal getRisultante_filtrato() {
        return risultante_filtrato;
    }

    public void setRisultante_filtrato(BigDecimal risultante_filtrato) {
        this.risultante_filtrato = risultante_filtrato;
    }

    public BigDecimal getRisultante_lineare() {
        return risultante_lineare;
    }

    public void setRisultante_lineare(BigDecimal risultante_lineare) {
        this.risultante_lineare = risultante_lineare;
    }

    public Boolean getIstanziato() {
        return istanziato;
    }

    public void setIstanziato(Boolean istanziato) {
        this.istanziato = istanziato;
    }
}
