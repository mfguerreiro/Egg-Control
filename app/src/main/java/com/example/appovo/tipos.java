package com.example.appovo;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.appovo.database.DadosOpenHelper;
import com.example.appovo.dominio.entidades.Tipo;
import com.example.appovo.dominio.repositorio.EstoqueRepositorio;
import com.example.appovo.dominio.repositorio.TipoRepositorio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import java.util.List;

public class tipos extends AppCompatActivity {
    private SQLiteDatabase conexao; //conexão com banco
    private DadosOpenHelper dadosOpenHelper;
    private TipoAdapter tipoAdapter;
    private TipoRepositorio tipoRepositorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView lstDadosTipo = findViewById(R.id.lstDadosTipos);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Intent it = new Intent(tipos.this, cad_tipos.class);
                        startActivityForResult(it, 1);
            }
        });

        criarConexao();

        lstDadosTipo.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lstDadosTipo.setLayoutManager(linearLayoutManager);

        tipoRepositorio = new TipoRepositorio(conexao);
        List<Tipo> dados = tipoRepositorio.buscarTodos();
        tipoAdapter = new TipoAdapter(dados);

        lstDadosTipo.setAdapter(tipoAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RecyclerView lstDadosTipo = findViewById(R.id.lstDadosTipos);
        List<Tipo> dados = tipoRepositorio.buscarTodos();
        tipoAdapter = new TipoAdapter(dados);

        lstDadosTipo.setAdapter(tipoAdapter);
    }

    private void criarConexao(){
        try {
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();


        } catch(SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.lbl_ok, null);
            dlg.show();
        }
    }
}