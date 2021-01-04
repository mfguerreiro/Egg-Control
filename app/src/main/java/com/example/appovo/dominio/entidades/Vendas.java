package com.example.appovo.dominio.entidades;


import java.io.Serializable;

public class Vendas implements Serializable {
    public int cod_vendas;
    public int quantidade;
    public String data;
    public int lote;
    public String rota;

    public void Vendas(){
        cod_vendas = 0;
    }
}
