package com.example.controledeestoque.model;

public class EntregaItem {
    private int uniformeId;
    private String tipo;
    private int quantidade = 0;

    public EntregaItem(int uniformeId, String tipo) {
        this.uniformeId = uniformeId;
        this.tipo = tipo;
    }

    public int getUniformeId() {
        return uniformeId;
    }

    public String getTipo() {
        return tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
