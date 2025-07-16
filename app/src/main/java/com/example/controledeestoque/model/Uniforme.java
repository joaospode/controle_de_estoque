package com.example.controledeestoque.model;

public class Uniforme {
    public int id;
    private String tipo;
    private int ca;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCa() {
        return ca;
    }

    public void setCa(int ca) {
        this.ca = ca;
    }

    @Override
    public String toString() {
        return this.getTipo();
    }
}
