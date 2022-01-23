package br.ufc.smd.diario.model;

import java.io.Serializable;
import java.util.Date;

public class Evento implements Serializable {

    public String idEvento;

    public String tipoEvento;

    public String subEvento;

    public Date momento;

    public String duracao;

    public String observacao;

    public Evento() {}

    public Evento(String idEvento, String tipoEvento, String subEvento, Date momento, String duracao, String observacao) {
        this.idEvento   = idEvento;
        this.tipoEvento = tipoEvento;
        this.subEvento  = subEvento;
        this.momento    = momento;
        this.duracao    = duracao;
        this.observacao = observacao;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getSubEvento() {
        return subEvento;
    }

    public void setSubEvento(String subEvento) {
        this.subEvento = subEvento;
    }

    public Date getMomento() {
        return momento;
    }

    public void setMomento(Date momento) {
        this.momento = momento;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "  idEvento='"   + idEvento   + '\'' +
                ", tipoEvento='" + tipoEvento + '\'' +
                ", subEvento='"  + subEvento  + '\'' +
                ", momento="     + momento    +
                ", duracao='"    + duracao    + '\'' +
                ", observacao='" + observacao + '\'' +
                '}';
    }
}