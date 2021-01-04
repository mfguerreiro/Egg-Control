package com.example.appovo.dominio.repositorio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appovo.dominio.entidades.Rota;
import com.example.appovo.dominio.entidades.Tipo;

import java.util.ArrayList;
import java.util.List;

public class TipoRepositorio {
    private SQLiteDatabase conexao;
    public TipoRepositorio(SQLiteDatabase conexao){
        this.conexao = conexao;
    }

    public void inserir(Tipo tipo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("DESCRICAO", tipo.descricao);

        conexao.insertOrThrow("TIPO", null, contentValues);
    }

    public void excluir(int codigo){
        String[] parametros = new String[1];
        parametros[0] = String.valueOf(codigo);

        conexao.delete("TIPO", "COD_TIPO = ?", parametros);
    }

    public void alterar(Tipo tipo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("DESCRICAO", tipo.descricao);

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(tipo.cod_tipo);

        conexao.update("TIPO", contentValues, "COD_TIPO = ?", parametros);
    }

    public List<Tipo> buscarTodos(){
        List<Tipo> tipo = new ArrayList<Tipo>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COD_TIPO, DESCRICAO ");
        sql.append(" FROM TIPO ");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);

        if(resultado.getCount()>0){
            resultado.moveToFirst();
            do{
                Tipo objTipo = new Tipo();

                objTipo.cod_tipo = resultado.getInt(resultado.getColumnIndexOrThrow("COD_TIPO"));
                objTipo.descricao = resultado.getString(resultado.getColumnIndexOrThrow("DESCRICAO"));

                tipo.add(objTipo);

            }while(resultado.moveToNext());
        }
        return tipo;
    }
}
