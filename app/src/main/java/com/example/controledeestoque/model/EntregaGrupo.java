package com.example.controledeestoque.model;

import java.util.List;

public class EntregaGrupo {
    private String funcionario;
    private String dataEntrega;
    private List<HistoricoEntrega> itens;

    public EntregaGrupo(String funcionario, String dataEntrega, List<HistoricoEntrega> itens) {
        this.funcionario = funcionario;
        this.dataEntrega = dataEntrega;
        this.itens = itens;
    }

    public String getFuncionario() { return funcionario; }
    public String getDataEntrega() { return dataEntrega; }
    public List<HistoricoEntrega> getItens() { return itens; }
}