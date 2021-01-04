package com.example.appovo.dominio.entidades;

import java.io.Serializable;

public class Estoque implements Serializable {
    public int lote;
    public int quantidade;
    public String tipo;
    public double custo;
    public double venda;
    public String data;

    public void Estoque(){
        lote = 0;
    }


}
