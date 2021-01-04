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
import com.example.appovo.dominio.entidades.Tipo;
import com.example.appovo.dominio.repositorio.TipoRepositorio;
import com.example.appovo.dominio.repositorio.VendasRepositorio;

public class cad_tipos extends AppCompatActivity {
    TipoRepositorio tipoRepositorio;
    Tipo tipo;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_tipos);
        getSupportActionBar().hide();//remover titlebar

        Button btnTipo = findViewById(R.id.btnEnviarTipo);

        criarConexao();

        btnTipo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirmar();
            }
        });

        verificaParametro();

    }

    private void verificaParametro(){
        EditText edtTipo = findViewById(R.id.edtTipo);
        Bundle bundle = getIntent().getExtras();

        tipo = new Tipo();

        if(bundle != null && bundle.containsKey("TIPO")){
            tipo = (Tipo) bundle.getSerializable("TIPO");
            edtTipo.setText(tipo.descricao);
        }
    }

    private void confirmar(){
        if(validaCampos() == false){
            try {
                if(tipo.cod_tipo == 0){
                    tipoRepositorio.inserir(tipo);
                }else {
                    tipoRepositorio.alterar(tipo);
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
        EditText edtTipo = findViewById(R.id.edtTipo);
        String descricaoTipo = edtTipo.getText().toString();

        tipo.descricao = descricaoTipo;

        if(res = isCampoVazio(descricaoTipo)){
            edtTipo.requestFocus();
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

            tipoRepositorio = new TipoRepositorio(conexao);

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
                tipoRepositorio.excluir(tipo.cod_tipo);
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