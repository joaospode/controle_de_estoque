package com.example.controledeestoque.model;

public class HistoricoEntrega {
    private int id;
    private String funcionario;
    private String uniforme;
    private int quantidade;
    private String dataEntrega;

    public HistoricoEntrega(int id, String funcionario, String uniforme, int quantidade, String dataEntrega) {
        this.id = id;
        this.funcionario = funcionario;
        this.uniforme   = uniforme;
        this.quantidade  = quantidade;
        this.dataEntrega = dataEntrega;
    }

    // getters apenas (não precisa de setters se você nunca altera depois)
    public int getId()               { return id; }
    public String getFuncionario()   { return funcionario; }
    public String getUniforme()      { return uniforme; }
    public int getQuantidade()       { return quantidade; }
    public String getDataEntrega()   { return dataEntrega; }
}