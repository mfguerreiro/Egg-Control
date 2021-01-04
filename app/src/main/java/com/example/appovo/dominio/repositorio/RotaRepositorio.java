package com.example.appovo.dominio.repositorio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appovo.dominio.entidades.Estoque;
import com.example.appovo.dominio.entidades.Rota;
import com.example.appovo.dominio.entidades.Tipo;

import java.util.ArrayList;
import java.util.List;

public class RotaRepositorio {
    private SQLiteDatabase conexao;
    public RotaRepositorio(SQLiteDatabase conexao){
        this.conexao = conexao;
    }

    public void inserir(String rota){
        ContentValues contentValues = new ContentValues();
        contentValues.put("DESCRICAO", rota);

        conexao.insertOrThrow("ROTA", null, contentValues);
    }

    public void excluir(int codigo){
        String[] parametros = new String[1];
        parametros[0] = String.valueOf(codigo);

        conexao.delete("ROTA", "COD_ROTA = ?", parametros);
    }

    public void alterar(Rota rota){
        ContentValues contentValues = new ContentValues();
        contentValues.put("COD_ROTA", rota.cod_rota);
        contentValues.put("DESCRICAO", rota.descricao);

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(rota.cod_rota);

        conexao.update("ROTA", contentValues, "COD_ROTA = ?", parametros);
    }

    public List<Rota> buscarTodos(){
        List<Rota> rota = new ArrayList<Rota>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COD_ROTA, DESCRICAO ");
        sql.append(" FROM ROTA ");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);

        if(resultado.getCount()>0){
            resultado.moveToFirst();
            do{
                Rota objRota = new Rota();

                objRota.cod_rota = resultado.getInt(resultado.getColumnIndexOrThrow("COD_ROTA"));
                objRota.descricao = resultado.getString(resultado.getColumnIndexOrThrow("DESCRICAO"));

                rota.add(objRota);

            }while(resultado.moveToNext());
        }
        return rota;
    }
}
