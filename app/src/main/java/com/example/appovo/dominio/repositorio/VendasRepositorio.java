package com.example.appovo.dominio.repositorio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appovo.dominio.entidades.Estoque;
import com.example.appovo.dominio.entidades.Vendas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VendasRepositorio {
    private SQLiteDatabase conexao;
    public VendasRepositorio(SQLiteDatabase conexao){
        this.conexao = conexao;
    }

    public void inserir(Vendas vendas){
        ContentValues contentValues = new ContentValues();
        contentValues.put("QUANTIDADE", vendas.quantidade);
        contentValues.put("DATA", String.valueOf(vendas.data));
        contentValues.put("LOTE", vendas.lote);
        contentValues.put("ROTA", vendas.rota);


        conexao.insertOrThrow("VENDAS", null, contentValues);
    }

    public void excluir(int cod_vendas){
        String[] parametros = new String[1];
        parametros[0] = String.valueOf(cod_vendas);

        conexao.delete("VENDAS","COD_VENDA = ?", parametros);
    }

    public void alterar(Vendas vendas){
        ContentValues contentValues = new ContentValues();
        contentValues.put("QUANTIDADE", vendas.quantidade);
        contentValues.put("DATA", String.valueOf(vendas.data));
        contentValues.put("LOTE", vendas.lote);
        contentValues.put("ROTA", vendas.rota);

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(vendas.cod_vendas);

        conexao.update("VENDAS", contentValues, "COD_VENDA = ?", parametros);
    }

    public List<Vendas> buscarTodos(){

        List<Vendas> vend = new ArrayList<Vendas>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COD_VENDA, QUANTIDADE, DATA, LOTE, ROTA");
        sql.append("    FROM VENDAS");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);

        if(resultado.getCount()>0){
            resultado.moveToFirst();

            do{
                Vendas vendas = new Vendas();

                vendas.cod_vendas = resultado.getInt(resultado.getColumnIndexOrThrow("COD_VENDA"));
                vendas.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));
                vendas.data = resultado.getString((resultado.getColumnIndexOrThrow("DATA")));
                vendas.lote = resultado.getInt(resultado.getColumnIndexOrThrow("LOTE"));
                vendas.rota = resultado.getString(resultado.getColumnIndexOrThrow("ROTA"));

                vend.add(vendas);
            }while(resultado.moveToNext());
        }

        return vend;
    }

    public Vendas buscarVenda(int cod_vendas){
        Vendas vend = new Vendas();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COD_VENDA, QUANTIDADE, DATA, LOTE, ROTA");
        sql.append("    FROM VENDAS");
        sql.append("    WHERE COD_VENDA = ?");

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(cod_vendas);

        Cursor resultado = conexao.rawQuery(sql.toString(), parametros);

        if(resultado.getCount()>0){
            resultado.moveToFirst();

            vend.cod_vendas = resultado.getInt(resultado.getColumnIndexOrThrow("COD_VENDA"));
            vend.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));
            vend.data = resultado.getString((resultado.getColumnIndexOrThrow("DATA")));
            vend.lote = resultado.getInt(resultado.getColumnIndexOrThrow("LOTE"));
            vend.rota = resultado.getString(resultado.getColumnIndexOrThrow("ROTA"));

            return vend;
        }

        return null;
    }

    public List<Vendas> buscarLoteLista(int lote){
        List<Vendas> Ven = new ArrayList<Vendas>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COD_VENDA, QUANTIDADE, DATA, LOTE, ROTA");
        sql.append("    FROM VENDAS");
        sql.append("    WHERE LOTE = ?");

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(lote);

        Cursor resultado = conexao.rawQuery(sql.toString(), parametros);

        if (resultado.getCount() == 0){
            return null;
        } else {
            if(resultado.getCount()>0){
                resultado.moveToFirst();

                do{
                    Vendas ven = new Vendas();

                    ven.cod_vendas = resultado.getInt(resultado.getColumnIndexOrThrow("COD_VENDA"));
                    ven.lote = resultado.getInt(resultado.getColumnIndexOrThrow("LOTE"));
                    ven.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));
                    ven.rota = resultado.getString(resultado.getColumnIndexOrThrow("ROTA"));
                    ven.data = resultado.getString(resultado.getColumnIndexOrThrow("DATA"));

                    Ven.add(ven);
                }while(resultado.moveToNext());

            }
            if(Ven != null){
                return Ven;
            }else{
                return null;
            }
        }

    }

    public List<Vendas> buscarRotaLista(String rota){
        List<Vendas> Ven = new ArrayList<Vendas>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COD_VENDA, QUANTIDADE, DATA, LOTE, ROTA");
        sql.append("    FROM VENDAS");
        sql.append("    WHERE ROTA = ?");

        String[] parametros = new String[1];
        parametros[0] = rota;

        Cursor resultado = conexao.rawQuery(sql.toString(), parametros);

        if(resultado.getCount()>0){
            resultado.moveToFirst();

            do{
                Vendas ven = new Vendas();

                ven.cod_vendas = resultado.getInt(resultado.getColumnIndexOrThrow("COD_VENDA"));
                ven.lote = resultado.getInt(resultado.getColumnIndexOrThrow("LOTE"));
                ven.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));
                ven.rota = resultado.getString(resultado.getColumnIndexOrThrow("ROTA"));
                ven.data = resultado.getString(resultado.getColumnIndexOrThrow("DATA"));;

                Ven.add(ven);
            }while(resultado.moveToNext());

        }
        if(Ven != null){
            return Ven;
        }else{
            return null;
        }
    }

    public List<Vendas> buscarDataLista(String dataInicial, String dataFinal){
        List<Vendas> Ven = new ArrayList<Vendas>();
        StringBuilder sql = new StringBuilder();

        String[] parametros = new String[2];
        parametros[0] = dataInicial;
        parametros[1] = dataFinal;


        sql.append("SELECT COD_VENDA, QUANTIDADE, DATA, LOTE, ROTA");
        sql.append("    FROM VENDAS");
        sql.append("    WHERE strftime('%Y-%m-%d',DATA) BETWEEN '"+dataInicial+"' AND '"+dataFinal+"'");

        Cursor resultado = conexao.rawQuery(sql.toString(),null);

        if(resultado.getCount()>0){
            resultado.moveToFirst();

            do{
                Vendas ven = new Vendas();

                ven.cod_vendas = resultado.getInt(resultado.getColumnIndexOrThrow("COD_VENDA"));
                ven.lote = resultado.getInt(resultado.getColumnIndexOrThrow("LOTE"));
                ven.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));
                ven.rota = resultado.getString(resultado.getColumnIndexOrThrow("ROTA"));
                ven.data = resultado.getString(resultado.getColumnIndexOrThrow("DATA"));;

                Ven.add(ven);
            }while(resultado.moveToNext());

        }
        if(Ven != null){
            return Ven;
        }else{
            return null;
        }
    }

}
