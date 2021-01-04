package com.example.appovo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.appovo.database.DadosOpenHelper;
import com.example.appovo.dominio.repositorio.VendasRepositorio;

public class banco extends AppCompatActivity {
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banco);

        Button btn_import = findViewById(R.id.btn_import);
        Button btn_export = findViewById(R.id.btn_export);

        btn_import.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //botão para importar
            }

        });
    }

    private void criarConexao(){
        try {
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();
            Toast toast = Toast.makeText(this, R.string.toast_db, Toast.LENGTH_LONG); //caso queira testar conexao com banco de dados nesta tela
            toast.show();

        } catch(SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.lbl_ok, null);
            dlg.show();
        }
    }
}