package com.example.appovo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appovo.database.DadosOpenHelper;
import com.example.appovo.dominio.entidades.Rota;
import com.example.appovo.dominio.entidades.Tipo;
import com.example.appovo.dominio.repositorio.RotaRepositorio;
import com.example.appovo.dominio.repositorio.VendasRepositorio;

public class cad_rotas extends AppCompatActivity {
    Rota rota;
    RotaRepositorio rotaRepositorio;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_rotas);
        getSupportActionBar().hide();//remover titlebar

        criarConexao();
        verificaParametro();

        Button btnRota = findViewById(R.id.btnEnviarRota);
        btnRota.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirmar();
            }
        });
    }

    private void verificaParametro(){
        EditText edtRota = findViewById(R.id.edtRota);
        rota = new Rota();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("ROTA")){
            rota = (Rota) bundle.getSerializable("ROTA");
            edtRota.setText(rota.descricao);
        }
    }

    private void confirmar(){
        if(validaCampos() == false){
            try {
                if(rota.cod_rota == 0){
                    rotaRepositorio.inserir(rota.descricao);
                }else{
                    rotaRepositorio.alterar(rota);
                }


                Toast toast = Toast.makeText(this, "Informações salvas com sucesso!", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }catch(SQLException ex){
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle(R.string.erro);
                dlg.setMessage(ex.getMessage());
                dlg.setNeutralButton(R.string.lbl_ok, null);
                dlg.show();
            }
        }
    }

    private boolean validaCampos(){
        boolean res = false;
        EditText edtRota = findViewById(R.id.edtRota);
        String descricaoRota = edtRota.getText().toString();

        rota.descricao = descricaoRota;

        if(res = isCampoVazio(descricaoRota)){
            edtRota.requestFocus();
        }

        if(res){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Aviso");
            dlg.setMessage("Há campos em branco!");
            dlg.setNeutralButton("Ok",null);
            dlg.show();
        }
        return res;
    }

    private boolean isCampoVazio(String valor){
        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;
    }


    private void criarConexao(){
        try {
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();

            rotaRepositorio = new RotaRepositorio(conexao);

        } catch(SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.lbl_ok, null);
            dlg.show();
        }
    }

    public void excluir(View view){
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle("Aviso");
        dlg.setMessage("Tem certeza que deseja excluir?");
        dlg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                rotaRepositorio.excluir(rota.cod_rota);
                finish();
            }
        });
        dlg.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dlg.show();


    }
}