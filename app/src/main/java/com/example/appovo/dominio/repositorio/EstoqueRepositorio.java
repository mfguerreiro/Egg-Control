package com.example.appovo.dominio.repositorio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.appovo.dominio.entidades.Estoque;
import com.example.appovo.estoque;

import java.util.ArrayList;
import java.util.List;

public class EstoqueRepositorio {
    private SQLiteDatabase conexao;

    public EstoqueRepositorio(SQLiteDatabase conexao){
            this.conexao = conexao;
    }

    public void inserir(Estoque estoque){
        ContentValues contentValues = new ContentValues();
        contentValues.put("QUANTIDADE", estoque.quantidade);
        contentValues.put("TIPO", estoque.tipo);
        contentValues.put("CUSTO", estoque.custo);
        contentValues.put("VENDA", estoque.venda);
        contentValues.put("DATA", estoque.data);

        conexao.insertOrThrow("ESTOQUE", null, contentValues);

    }

    public void excluir(int lote){
        String[] parametros = new String[1];
        parametros[0] = String.valueOf(lote);

        conexao.delete("ESTOQUE", "LOTE = ?", parametros);
    }

    public void alterar(Estoque estoque){
        ContentValues contentValues = new ContentValues();
        contentValues.put("QUANTIDADE", estoque.quantidade);
        contentValues.put("TIPO", estoque.tipo);
        contentValues.put("CUSTO", estoque.custo);
        contentValues.put("VENDA", estoque.venda);
        contentValues.put("DATA", estoque.data);

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(estoque.lote);

        conexao.update("ESTOQUE", contentValues, "LOTE = ?", parametros);
    }

    public List<Estoque> consultarTodos(){
        List<Estoque> Est = new ArrayList<Estoque>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT LOTE, QUANTIDADE, TIPO, CUSTO, VENDA, DATA");
        sql.append("    FROM ESTOQUE");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);

        if(resultado.getCount()>0){
            resultado.moveToFirst();

            do{
                Estoque est = new Estoque();

                est.lote = resultado.getInt(resultado.getColumnIndexOrThrow("LOTE"));
                est.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));
                est.tipo = resultado.getString(resultado.getColumnIndexOrThrow("TIPO"));
                est.custo = resultado.getDouble(resultado.getColumnIndexOrThrow("CUSTO"));
                est.venda = resultado.getDouble(resultado.getColumnIndexOrThrow("VENDA"));
                est.data = resultado.getString(resultado.getColumnIndexOrThrow("DATA"));

                Est.add(est);
            }while(resultado.moveToNext());
        }

        return Est;
    }

    public List<Estoque> buscarLoteLista(int lote){
        List<Estoque> Est = new ArrayList<Estoque>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT LOTE, QUANTIDADE, TIPO, CUSTO, VENDA, DATA");
        sql.append("    FROM ESTOQUE");
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
                    Estoque est = new Estoque();

                    est.lote = resultado.getInt(resultado.getColumnIndexOrThrow("LOTE"));
                    est.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));
                    est.tipo = resultado.getString(resultado.getColumnIndexOrThrow("TIPO"));
                    est.custo = resultado.getDouble(resultado.getColumnIndexOrThrow("CUSTO"));
                    est.venda = resultado.getDouble(resultado.getColumnIndexOrThrow("VENDA"));
                    est.data = resultado.getString(resultado.getColumnIndexOrThrow("DATA"));

                    Est.add(est);
                }while(resultado.moveToNext());

            }
            if(Est != null){
                return Est;
            }else{
                return null;
            }
        }

    }

    public Estoque buscarLote(int lote){
        Estoque Est = new Estoque();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT LOTE, QUANTIDADE, TIPO, CUSTO, VENDA, DATA");
        sql.append("    FROM ESTOQUE");
        sql.append("    WHERE LOTE = ?");

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(lote);

        Cursor resultado = conexao.rawQuery(sql.toString(), parametros);

        if(resultado.getCount()>0){
            resultado.moveToFirst();

                Est.lote = resultado.getInt(resultado.getColumnIndexOrThrow("LOTE"));
                Est.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));
                Est.tipo = resultado.getString(resultado.getColumnIndexOrThrow("TIPO"));
                Est.custo = resultado.getDouble(resultado.getColumnIndexOrThrow("CUSTO"));
                Est.venda = resultado.getDouble(resultado.getColumnIndexOrThrow("VENDA"));
                Est.data = resultado.getString(resultado.getColumnIndexOrThrow("DATA"));

            return Est;
        }

        return null;
    }

    public List<Estoque> buscarTipoLista(String tipo){
        List<Estoque> Est = new ArrayList<Estoque>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT LOTE, QUANTIDADE, TIPO, CUSTO, VENDA, DATA");
        sql.append("    FROM ESTOQUE");
        sql.append("    WHERE TIPO = ?");

        String[] parametros = new String[1];
        parametros[0] = tipo;

        Cursor resultado = conexao.rawQuery(sql.toString(), parametros);

        if(resultado.getCount()>0){
            resultado.moveToFirst();

            do{
                Estoque est = new Estoque();

                est.lote = resultado.getInt(resultado.getColumnIndexOrThrow("LOTE"));
                est.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));
                est.tipo = resultado.getString(resultado.getColumnIndexOrThrow("TIPO"));
                est.custo = resultado.getDouble(resultado.getColumnIndexOrThrow("CUSTO"));
                est.venda = resultado.getDouble(resultado.getColumnIndexOrThrow("VENDA"));
                est.data = resultado.getString(resultado.getColumnIndexOrThrow("DATA"));

                Est.add(est);
            }while(resultado.moveToNext());

        }
        if(Est != null){
            return Est;
        }else{
            return null;
        }
    }

    public List<Estoque> buscarDataLista(String dataInicial, String dataFinal){
        List<Estoque> Est = new ArrayList<Estoque>();
        StringBuilder sql = new StringBuilder();

        String[] parametros = new String[2];
        parametros[0] = dataInicial;
        parametros[1] = dataFinal;


        sql.append("SELECT LOTE, QUANTIDADE, TIPO, CUSTO, VENDA, DATA");
        sql.append("    FROM ESTOQUE");
        sql.append("    WHERE strftime('%Y-%m-%d',DATA) BETWEEN '"+dataInicial+"' AND '"+dataFinal+"'");

        Cursor resultado = conexao.rawQuery(sql.toString(),null);

        if(resultado.getCount()>0){
            resultado.moveToFirst();

            do{
                Estoque est = new Estoque();

                est.lote = resultado.getInt(resultado.getColumnIndexOrThrow("LOTE"));
                est.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));
                est.tipo = resultado.getString(resultado.getColumnIndexOrThrow("TIPO"));
                est.custo = resultado.getDouble(resultado.getColumnIndexOrThrow("CUSTO"));
                est.venda = resultado.getDouble(resultado.getColumnIndexOrThrow("VENDA"));
                est.data = resultado.getString(resultado.getColumnIndexOrThrow("DATA"));

                Est.add(est);
            }while(resultado.moveToNext());

        }
        if(Est != null){
            return Est;
        }else{
            return null;
        }
    }
}
