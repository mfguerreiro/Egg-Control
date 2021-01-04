package com.example.appovo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appovo.database.DadosOpenHelper;
import com.example.appovo.dominio.entidades.Estoque;
import com.example.appovo.dominio.repositorio.EstoqueRepositorio;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

public class dados_estoque extends AppCompatActivity {
    private SQLiteDatabase conexao; //conexão com banco
    private DadosOpenHelper dadosOpenHelper;
    private EstoqueRepositorio estoqueRepositorio;
    private EstoqueAdapter estoqueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//remover titlebar
        setContentView(R.layout.activity_dados_estoque);
        DecimalFormat df = new DecimalFormat("#,###.00"); //converter para apenas 2 casas decimais depois da virgula

        Bundle bundle = getIntent().getExtras();
        int codBotao = (int) bundle.getSerializable("CODIGOBOTAO");

        RecyclerView lstDados = findViewById(R.id.lstDados);

        ConstraintLayout layoutContentMain = findViewById(R.id.LayoutContentMain);

        criarConexao();

        lstDados.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lstDados.setLayoutManager(linearLayoutManager);

        estoqueRepositorio = new EstoqueRepositorio(conexao);
            if(codBotao == 1){

                List<Estoque> dados = estoqueRepositorio.consultarTodos();
                estoqueAdapter = new EstoqueAdapter(dados);
                lstDados.setAdapter(estoqueAdapter);

                //Calcular totais
                    Cursor resultado = conexao.rawQuery("SELECT LOTE, QUANTIDADE, TIPO, CUSTO, VENDA, DATA FROM ESTOQUE", null);

                    int totalLote = resultado.getCount();
                    int totalQtd = 0;
                    Double totalCusto = Double.valueOf(0);

                    if(resultado.getCount()>0){
                        resultado.moveToFirst();

                        do{
                            Estoque est = new Estoque();

                            est.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));
                            est.custo = resultado.getDouble(resultado.getColumnIndexOrThrow("CUSTO"));

                            totalQtd = totalQtd + est.quantidade;
                            totalCusto = totalCusto + est.custo;
                        }while(resultado.moveToNext());

                        TextView txtTotalQtd = findViewById(R.id.txtTotalQtd);
                        TextView txtTotalLote = findViewById(R.id.txtTotalLote);
                        TextView txtTotalCusto = findViewById(R.id.txtTotalCusto);
                        txtTotalQtd.setText(String.valueOf(totalQtd));
                        txtTotalLote.setText(String.valueOf(totalLote));


                        txtTotalCusto.setText(String.valueOf(df.format(totalCusto)));

                    }

            }else if(codBotao == 2){
                int lote = (int) bundle.getSerializable("LOTE");
                List<Estoque> dados = estoqueRepositorio.buscarLoteLista(lote);

                if (dados == null){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                    dlg.setTitle("Aviso");
                    dlg.setMessage("LOTE INEXISTENTE");
                    dlg.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent it = new Intent(dados_estoque.this, cons_estoque.class);
                            startActivity(it);
                        }
                    });
                    dlg.show();

                }else{
                    estoqueAdapter = new EstoqueAdapter(dados);
                    lstDados.setAdapter(estoqueAdapter);

                    //Calcular totais
                    String[] parametros = new String[1];
                    parametros[0] = String.valueOf(lote);
                    Cursor resultado = conexao.rawQuery("SELECT LOTE, QUANTIDADE, TIPO, CUSTO, VENDA, DATA FROM ESTOQUE WHERE LOTE = ?", parametros);

                    int totalLote = resultado.getCount();
                    int totalQtd = 0;
                    Double totalCusto = Double.valueOf(0);

                    if(resultado.getCount()>0){
                        resultado.moveToFirst();

                        do{
                            Estoque est = new Estoque();

                            est.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));
                            est.custo = resultado.getDouble(resultado.getColumnIndexOrThrow("CUSTO"));

                            totalQtd = totalQtd + est.quantidade;
                            totalCusto = totalCusto + est.custo;
                        }while(resultado.moveToNext());

                        TextView txtTotalQtd = findViewById(R.id.txtTotalQtd);
                        TextView txtTotalLote = findViewById(R.id.txtTotalLote);
                        TextView txtTotalCusto = findViewById(R.id.txtTotalCusto);
                        txtTotalQtd.setText(String.valueOf(totalQtd));
                        txtTotalLote.setText(String.valueOf(totalLote));
                        txtTotalCusto.setText(String.valueOf(df.format(totalCusto)));

                    }

                }


            }else if(codBotao == 3){
                String tipo = (String) bundle.get("TIPO");
                List<Estoque> dados = estoqueRepositorio.buscarTipoLista(tipo);

                if (dados.isEmpty()){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                    dlg.setTitle("Aviso");
                    dlg.setMessage("ESSA BUSCA NÃO RETORNOU NENHUM DADO.");
                    dlg.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent it = new Intent(dados_estoque.this, cons_estoque.class);
                            startActivity(it);
                        }
                    });
                    dlg.show();

                }else {
                    estoqueAdapter = new EstoqueAdapter(dados);
                    lstDados.setAdapter(estoqueAdapter);

                    //Calcular totais
                    String[] parametros = new String[1];
                    parametros[0] = String.valueOf(tipo);
                    Cursor resultado = conexao.rawQuery("SELECT LOTE, QUANTIDADE, TIPO, CUSTO, VENDA, DATA FROM ESTOQUE WHERE TIPO = ?", parametros);

                    int totalLote = resultado.getCount();
                    int totalQtd = 0;
                    Double totalCusto = Double.valueOf(0);

                    if (resultado.getCount() > 0) {
                        resultado.moveToFirst();

                        do {
                            Estoque est = new Estoque();

                            est.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));
                            est.custo = resultado.getDouble(resultado.getColumnIndexOrThrow("CUSTO"));

                            totalQtd = totalQtd + est.quantidade;
                            totalCusto = totalCusto + est.custo;
                        } while (resultado.moveToNext());

                        TextView txtTotalQtd = findViewById(R.id.txtTotalQtd);
                        TextView txtTotalLote = findViewById(R.id.txtTotalLote);
                        TextView txtTotalCusto = findViewById(R.id.txtTotalCusto);
                        txtTotalQtd.setText(String.valueOf(totalQtd));
                        txtTotalLote.setText(String.valueOf(totalLote));
                        txtTotalCusto.setText(String.valueOf(df.format(totalCusto)));

                    }

                }
            }else if(codBotao == 4){
                String dataInicial = (String) bundle.getSerializable("DATAINICIAL");
                String dataFinal = (String) bundle.getSerializable("DATAFINAL");

                List<Estoque> dados = estoqueRepositorio.buscarDataLista(dataInicial, dataFinal);

                if (dados.isEmpty()){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                    dlg.setTitle("Aviso");
                    dlg.setMessage("ESSA BUSCA NÃO RETORNOU NENHUM DADO.");
                    dlg.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent it = new Intent(dados_estoque.this, cons_estoque.class);
                            startActivity(it);
                        }
                    });
                    dlg.show();

                }else {
                    estoqueAdapter = new EstoqueAdapter(dados);
                    lstDados.setAdapter(estoqueAdapter);

                    //Calcular totais
                    StringBuilder sql = new StringBuilder();
                    sql.append("SELECT LOTE, QUANTIDADE, TIPO, CUSTO, VENDA, DATA");
                    sql.append("    FROM ESTOQUE");
                    sql.append("    WHERE strftime('%Y-%m-%d',DATA) BETWEEN '" + dataInicial + "' AND '" + dataFinal + "'");

                    Cursor resultado = conexao.rawQuery(sql.toString(), null);

                    int totalLote = resultado.getCount();
                    int totalQtd = 0;
                    Double totalCusto = Double.valueOf(0);

                    if (resultado.getCount() > 0) {
                        resultado.moveToFirst();

                        do {
                            Estoque est = new Estoque();

                            est.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));
                            est.custo = resultado.getDouble(resultado.getColumnIndexOrThrow("CUSTO"));

                            totalQtd = totalQtd + est.quantidade;
                            totalCusto = totalCusto + est.custo;
                        } while (resultado.moveToNext());

                        TextView txtTotalQtd = findViewById(R.id.txtTotalQtd);
                        TextView txtTotalLote = findViewById(R.id.txtTotalLote);
                        TextView txtTotalCusto = findViewById(R.id.txtTotalCusto);
                        txtTotalQtd.setText(String.valueOf(totalQtd));
                        txtTotalLote.setText(String.valueOf(totalLote));
                        txtTotalCusto.setText(String.valueOf(df.format(totalCusto)));

                    }
                }
            }else{
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle(R.string.erro);
                dlg.setMessage("Nenhum resultado!");
                dlg.setNeutralButton(R.string.lbl_ok, null);
                dlg.show();
                finish();
            }


    }

    public void criarConexao(){
        try {
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();

            estoqueRepositorio = new EstoqueRepositorio(conexao);

        } catch(SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.lbl_ok, null);
            dlg.show();

        }

    }
}