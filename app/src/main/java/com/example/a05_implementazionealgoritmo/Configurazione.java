package com.example.a05_implementazionealgoritmo;

import java.io.Serializable;
import java.math.BigDecimal;

public class Configurazione implements Serializable, Cloneable {

    private BigDecimal[][] matrice_di_rotazione; //3x3
    private BigDecimal ultimo_massimo_locale_accelerometro_valore;
    private BigDecimal ultimo_minimo_locale_accelerometro_valore;
    private Long istante_seconda_parte_ultimo_passo;
    private Long istante_prima_parte_ultimo_passo;
    private Long ultima_intersezione_asse_ascisse_istante;
    private Integer frequenza_di_campionamento, real_time, algoritmo_di_riconoscimento, filtro, frequenza_di_taglio;
    private BigDecimal accelerazione_di_gravita;
    private Long istante_ultimo_passo_individuato;
    private Boolean individuazione_continue_bagilevi;
    private BigDecimal individuazione_threshold;

    /*
        frequenzaDiCampionamento
            0: 20Hz
            1: 40Hz
            2: 50Hz
            3: 100Hz
            4: MAX DISPONIBILE
        realTime
            0: REAL-TIME
            1: NON REAL-TIME
        algoritmoDiRiconoscimento
            0: ALGORITMO PICCHI
            1: ALGORITMO PICCHI + Correzione con ALGORITMO INTERSEZIONI
        filtro
            0: FILTRO BAGILEVI
            1: FILTRO PASSA-BASSO
            2: NESSUN FILTRO
            3: MATRICE ROTAZIONE
        frequenzaDiTaglio
            0: 2Hz
            1: 3Hz
            2: 10Hz
            3: alpha=0.1
     */

    public Configurazione() {
        matrice_di_rotazione = new BigDecimal[][]{{BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0)}, {BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0)}, {BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0)}};
        ultimo_massimo_locale_accelerometro_valore=BigDecimal.valueOf(0);
        istante_seconda_parte_ultimo_passo =Long.valueOf(0);
        ultimo_minimo_locale_accelerometro_valore=BigDecimal.valueOf(0);
        istante_prima_parte_ultimo_passo =Long.valueOf(0);
        ultima_intersezione_asse_ascisse_istante=Long.valueOf(0);
        frequenza_di_campionamento=-1;
        real_time=-1;
        algoritmo_di_riconoscimento=-1;
        filtro=-1;
        frequenza_di_taglio=-1;
        accelerazione_di_gravita= BigDecimal.valueOf(9.80665);
    }

    public BigDecimal[][] getMatrice_di_rotazione() {
        return matrice_di_rotazione;
    }

    public void setMatrice_di_rotazione(BigDecimal[][] matrice_di_rotazione) {
        this.matrice_di_rotazione = matrice_di_rotazione;
    }

    public BigDecimal getUltimo_massimo_locale_accelerometro_valore() {
        return ultimo_massimo_locale_accelerometro_valore;
    }

    public void setUltimo_massimo_locale_accelerometro_valore(BigDecimal ultimo_massimo_locale_accelerometro_valore) {
        this.ultimo_massimo_locale_accelerometro_valore = ultimo_massimo_locale_accelerometro_valore;
    }

    public Long getIstante_seconda_parte_ultimo_passo() {
        return istante_seconda_parte_ultimo_passo;
    }

    public void setIstante_seconda_parte_ultimo_passo(Long istante_seconda_parte_ultimo_passo) {
        this.istante_seconda_parte_ultimo_passo = istante_seconda_parte_ultimo_passo;
    }

    public BigDecimal getUltimo_minimo_locale_accelerometro_valore() {
        return ultimo_minimo_locale_accelerometro_valore;
    }

    public void setUltimo_minimo_locale_accelerometro_valore(BigDecimal ultimo_minimo_locale_accelerometro_valore) {
        this.ultimo_minimo_locale_accelerometro_valore = ultimo_minimo_locale_accelerometro_valore;
    }

    public Long getIstante_prima_parte_ultimo_passo() {
        return istante_prima_parte_ultimo_passo;
    }

    public void setIstante_prima_parte_ultimo_passo(Long istante_prima_parte_ultimo_passo) {
        this.istante_prima_parte_ultimo_passo = istante_prima_parte_ultimo_passo;
    }

    public Long getUltima_intersezione_asse_ascisse_istante() {
        return ultima_intersezione_asse_ascisse_istante;
    }

    public void setUltima_intersezione_asse_ascisse_istante(Long ultima_intersezione_asse_ascisse_istante) {
        this.ultima_intersezione_asse_ascisse_istante = ultima_intersezione_asse_ascisse_istante;
    }

    public Integer getFrequenza_di_campionamento() {
        return frequenza_di_campionamento;
    }

    public void setFrequenza_di_campionamento(Integer frequenza_di_campionamento) {
        this.frequenza_di_campionamento = frequenza_di_campionamento;
    }

    public Integer getReal_time() {
        return real_time;
    }

    public void setReal_time(Integer real_time) {
        this.real_time = real_time;
    }

    public Integer getAlgoritmo_di_riconoscimento() {
        return algoritmo_di_riconoscimento;
    }

    public void setAlgoritmo_di_riconoscimento(Integer algoritmo_di_riconoscimento) {
        this.algoritmo_di_riconoscimento = algoritmo_di_riconoscimento;
    }

    public Integer getFiltro() {
        return filtro;
    }

    public void setFiltro(Integer filtro) {
        this.filtro = filtro;
    }

    public Integer getFrequenza_di_taglio() {
        return frequenza_di_taglio;
    }

    public void setFrequenza_di_taglio(Integer frequenza_di_taglio) {
        this.frequenza_di_taglio = frequenza_di_taglio;
    }

    public BigDecimal getAccelerazione_di_gravita() {
        return accelerazione_di_gravita;
    }

    public void setAccelerazione_di_gravita(BigDecimal accelerazione_di_gravita) {
        this.accelerazione_di_gravita = accelerazione_di_gravita;
    }

    public Boolean getIndividuazione_continue_bagilevi() {
        return individuazione_continue_bagilevi;
    }

    public void setIndividuazione_continue_bagilevi(Boolean individuazione_continue_bagilevi) {
        this.individuazione_continue_bagilevi = individuazione_continue_bagilevi;
    }

    public BigDecimal get_differenza_ultimo_massimo_minimo_locale_accelerometro_valore(){
        return ultimo_massimo_locale_accelerometro_valore.subtract(ultimo_minimo_locale_accelerometro_valore).abs();
    }

    public BigDecimal getIndividuazione_threshold() {
        return individuazione_threshold;
    }

    public void setIndividuazione_threshold(BigDecimal individuazione_threshold) {
        this.individuazione_threshold = individuazione_threshold;
    }

    public Long getIstante_ultimo_passo_individuato() {
        return istante_ultimo_passo_individuato;
    }

    public void setIstante_ultimo_passo_individuato(Long istante_ultimo_passo_individuato) {
        this.istante_ultimo_passo_individuato = istante_ultimo_passo_individuato;
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
