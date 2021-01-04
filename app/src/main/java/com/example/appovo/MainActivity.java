package com.example.appovo;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.appovo.database.DadosOpenHelper;
import com.example.appovo.dominio.entidades.Estoque;
import com.example.appovo.dominio.entidades.Rota;
import com.example.appovo.dominio.entidades.Tipo;
import com.example.appovo.dominio.repositorio.EstoqueRepositorio;
import com.example.appovo.dominio.repositorio.RotaRepositorio;
import com.example.appovo.dominio.repositorio.TipoRepositorio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout layout_vendas;
    private SQLiteDatabase conexao; //conexão com banco
    private DadosOpenHelper dadosOpenHelper;
    private EstoqueRepositorio estoqueRepositorio;
    private RotaRepositorio rotaRepositorio;
    private TipoRepositorio tipoRepositorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        //conexão com banco
        criarConexao();

        //botão vendas link para tela vendas
        Button btn_Vendas = (Button) findViewById(R.id.btn_vendas);

        btn_Vendas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, cad_Vendas.class);
                estoqueRepositorio = new EstoqueRepositorio(conexao);
                List<Estoque> dadosEstoque = estoqueRepositorio.consultarTodos();

                rotaRepositorio = new RotaRepositorio(conexao);
                List<Rota> dadosRota = rotaRepositorio.buscarTodos();
                if (dadosEstoque.isEmpty() ){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle("Aviso");
                    dlg.setMessage("NÃO HÁ NENHUM LOTE CADASTRADO!");
                    dlg.setNeutralButton("Ok", null);
                    dlg.show();
                }else if (dadosRota.isEmpty()){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle("Aviso");
                    dlg.setMessage("NÃO HÁ NENHUMA ROTA CADASTRADA!");
                    dlg.setNeutralButton("Ok", null);
                    dlg.show();
                }else {
                    startActivity(it);
                }
            }
        });
        //botão estoque link para tela estoque
        Button btn_estoque = (Button) findViewById(R.id.btn_estoque);

        btn_estoque.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, cad_estoque.class);
                tipoRepositorio = new TipoRepositorio(conexao);
                List<Tipo> dadosTipo = tipoRepositorio.buscarTodos();
                if (dadosTipo.isEmpty()){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle("Aviso");
                    dlg.setMessage("NÃO HÁ NENHUM TIPO CADASTRADO!");
                    dlg.setNeutralButton("Ok", null);
                    dlg.show();
                }else {
                    startActivity(it);
                }


            }
        });
        //botão relatório link para tela relatorios
        Button btn_relatorio = (Button) findViewById(R.id.btn_relatorio);

        btn_relatorio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, relatorios.class);
                startActivity(it);
            }
        });

        //botão relatório link para tela configurações
        Button btn_config = (Button) findViewById(R.id.btn_config);

        btn_config.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, configuracoes.class);
                startActivity(it);
            }
        });

        layout_vendas = (ConstraintLayout) findViewById(R.id.linearLayoutVendas);
    }
    //conexão com banco de dados
    private void criarConexao(){
        try {
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();
            Toast toast = Toast.makeText(this, R.string.toast_db, Toast.LENGTH_LONG);
            toast.show();
        } catch(SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.lbl_ok, null);
            dlg.show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}