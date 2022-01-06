package br.ufc.smd.diario.model;

import java.util.Date;

public class Notificacao {

    private String descricao;

    private Date dataCadastro;

    private boolean habilitado;

    public Notificacao() {}

    public Notificacao(String descricao, Date dataCadastro, boolean habilitado) {
        this.descricao = descricao;
        this.dataCadastro = dataCadastro;
        this.habilitado = habilitado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
}
