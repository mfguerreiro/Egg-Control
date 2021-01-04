package com.example.appovo;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.appovo.database.DadosOpenHelper;
import com.example.appovo.dominio.entidades.Rota;
import com.example.appovo.dominio.repositorio.EstoqueRepositorio;
import com.example.appovo.dominio.repositorio.RotaRepositorio;
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

public class rotas extends AppCompatActivity {
    private SQLiteDatabase conexao; //conexão com banco
    private DadosOpenHelper dadosOpenHelper;
    private RotaAdapter rotaAdapter;
    private RotaRepositorio rotaRepositorio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView lstDadosRota = findViewById(R.id.lstDadosRotas);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(rotas.this, cad_rotas.class);
                startActivityForResult(it, 2);
            }
        });

        criarConexao();
        lstDadosRota.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lstDadosRota.setLayoutManager(linearLayoutManager);

        rotaRepositorio = new RotaRepositorio(conexao);
        List<Rota> dados = rotaRepositorio.buscarTodos();
        rotaAdapter = new RotaAdapter(dados);

        lstDadosRota.setAdapter(rotaAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        RecyclerView lstDadosRota = findViewById(R.id.lstDadosRotas);
        super.onActivityResult(requestCode, resultCode, data);
        List<Rota> dados = rotaRepositorio.buscarTodos();
        rotaAdapter = new RotaAdapter(dados);

        lstDadosRota.setAdapter(rotaAdapter);
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