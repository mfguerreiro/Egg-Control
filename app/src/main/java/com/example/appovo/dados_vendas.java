package com.example.appovo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.appovo.dominio.entidades.Vendas;
import com.example.appovo.dominio.repositorio.VendasRepositorio;

import java.text.DecimalFormat;
import java.util.List;


public class dados_vendas extends AppCompatActivity {
    public SQLiteDatabase conexao; //conexão com banco
    private DadosOpenHelper dadosOpenHelper;
    private VendasRepositorio vendasRepositorio;
    private VendasAdapter vendasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//remover titlebar
        DecimalFormat df = new DecimalFormat("#,###.00"); //converter para apenas 2 casas decimais depois da virgula
        setContentView(R.layout.activity_dados_vendas);
        RecyclerView lstDados = findViewById(R.id.lstDados);

        TextView txtTipo = findViewById(R.id.txtTipo);
        TextView txtVenda = findViewById(R.id.txtTipo);
        TextView txtCusto = findViewById(R.id.txtQtdDisp);

        criarConexao();

        Bundle bundle = getIntent().getExtras();
        int codBotao = (int) bundle.getSerializable("CODIGOBOTAO");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lstDados.setLayoutManager(linearLayoutManager);

        vendasRepositorio = new VendasRepositorio(conexao);

        if (codBotao == 1) {
            List<Vendas> dados = vendasRepositorio.buscarTodos();
            vendasAdapter = new VendasAdapter(dados, conexao);
            lstDados.setAdapter(vendasAdapter);

            //Calcular totais
            Cursor resultado = conexao.rawQuery("SELECT COD_VENDA, QUANTIDADE, DATA, LOTE, ROTA FROM VENDAS", null);


            int totalVendas = resultado.getCount();
            int totalQtd = 0;
            Double totalLucro = Double.valueOf(0);

            if(resultado.getCount()>0 ){
                resultado.moveToFirst();
                do{
                    Vendas ven = new Vendas();
                    Estoque est = new Estoque();
                    ven.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));

                    String[] parametros = new String[1];
                    parametros[0] = String.valueOf(resultado.getInt(resultado.getColumnIndexOrThrow("LOTE")));

                    Cursor resultadoEstoque = conexao.rawQuery("SELECT CUSTO, VENDA FROM ESTOQUE WHERE LOTE = ?" , parametros);
                    resultadoEstoque.moveToFirst();

                    est.custo = resultadoEstoque.getDouble(resultadoEstoque.getColumnIndexOrThrow("CUSTO")) * ven.quantidade;
                    est.venda = resultadoEstoque.getDouble(resultadoEstoque.getColumnIndexOrThrow("VENDA")) * ven.quantidade;

                    totalQtd = totalQtd + ven.quantidade;
                    totalLucro = totalLucro + (est.venda - est.custo);

                }while(resultado.moveToNext());

                TextView txtTotalQtd = findViewById(R.id.txtTotalQtdVendas);
                TextView txtTotalVendas = findViewById(R.id.txtTotalVendas);
                TextView txtTotalLucro = findViewById(R.id.txtTotalLucro);

                txtTotalVendas.setText(String.valueOf(totalVendas));
                txtTotalQtd.setText(String.valueOf(totalQtd));

                txtTotalLucro.setText(String.valueOf(df.format(totalLucro)));

            }


        } else if (codBotao == 2) {
            int lote = (int) bundle.getSerializable("LOTE");
            List<Vendas> dados = vendasRepositorio.buscarLoteLista(lote);

            if (dados == null) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle("Aviso");
                dlg.setMessage("Não existe nenhuma venda do lote informado.");
                dlg.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent it = new Intent(dados_vendas.this, cons_vendas.class);
                        startActivity(it);
                    }
                });
                dlg.show();

            } else {
                vendasAdapter = new VendasAdapter(dados, conexao);
                lstDados.setAdapter(vendasAdapter);

                //Calcular totais
                Cursor resultado = conexao.rawQuery("SELECT COD_VENDA, QUANTIDADE, DATA, LOTE, ROTA FROM VENDAS WHERE LOTE = "+ lote, null);


                int totalVendas = resultado.getCount();
                int totalQtd = 0;
                Double totalLucro = Double.valueOf(0);

                if(resultado.getCount()>0 ){
                    resultado.moveToFirst();
                    do{
                        Vendas ven = new Vendas();
                        Estoque est = new Estoque();
                        ven.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));

                        String[] parametros = new String[1];
                        parametros[0] = String.valueOf(resultado.getInt(resultado.getColumnIndexOrThrow("LOTE")));

                        Cursor resultadoEstoque = conexao.rawQuery("SELECT CUSTO, VENDA FROM ESTOQUE WHERE LOTE = ?" , parametros);
                        resultadoEstoque.moveToFirst();

                        est.custo = resultadoEstoque.getDouble(resultadoEstoque.getColumnIndexOrThrow("CUSTO")) * ven.quantidade;
                        est.venda = resultadoEstoque.getDouble(resultadoEstoque.getColumnIndexOrThrow("VENDA")) * ven.quantidade;

                        totalQtd = totalQtd + ven.quantidade;
                        totalLucro = totalLucro + (est.venda - est.custo);

                    }while(resultado.moveToNext());

                    TextView txtTotalQtd = findViewById(R.id.txtTotalQtdVendas);
                    TextView txtTotalVendas = findViewById(R.id.txtTotalVendas);
                    TextView txtTotalLucro = findViewById(R.id.txtTotalLucro);

                    txtTotalVendas.setText(String.valueOf(totalVendas));
                    txtTotalQtd.setText(String.valueOf(totalQtd));

                    txtTotalLucro.setText(String.valueOf(df.format(totalLucro)));

                }

            }
        } else if (codBotao == 3) {

            String rota = (String) bundle.get("ROTA");
            List<Vendas> dados = vendasRepositorio.buscarRotaLista(rota);

            if (dados.isEmpty()) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle("Aviso");
                dlg.setMessage("ESSA BUSCA NÃO RETORNOU NENHUM RESULTADO.");
                dlg.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent it = new Intent(dados_vendas.this, cons_vendas.class);
                        startActivity(it);
                    }
                });
                dlg.show();

            } else {
                vendasAdapter = new VendasAdapter(dados, conexao);
                lstDados.setAdapter(vendasAdapter);

                //Calcular totais
                Cursor resultado = conexao.rawQuery("SELECT COD_VENDA, QUANTIDADE, DATA, LOTE, ROTA FROM VENDAS WHERE ROTA = '" + rota +"'", null);


                int totalVendas = resultado.getCount();
                int totalQtd = 0;
                Double totalLucro = Double.valueOf(0);

                if(resultado.getCount()>0 ){
                    resultado.moveToFirst();
                    do{
                        Vendas ven = new Vendas();
                        Estoque est = new Estoque();
                        ven.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));

                        String[] parametros = new String[1];
                        parametros[0] = String.valueOf(resultado.getInt(resultado.getColumnIndexOrThrow("LOTE")));

                        Cursor resultadoEstoque = conexao.rawQuery("SELECT CUSTO, VENDA FROM ESTOQUE WHERE LOTE = ?" , parametros);
                        resultadoEstoque.moveToFirst();

                        est.custo = resultadoEstoque.getDouble(resultadoEstoque.getColumnIndexOrThrow("CUSTO")) * ven.quantidade;
                        est.venda = resultadoEstoque.getDouble(resultadoEstoque.getColumnIndexOrThrow("VENDA")) * ven.quantidade;

                        totalQtd = totalQtd + ven.quantidade;
                        totalLucro = totalLucro + (est.venda - est.custo);

                    }while(resultado.moveToNext());

                    TextView txtTotalQtd = findViewById(R.id.txtTotalQtdVendas);
                    TextView txtTotalVendas = findViewById(R.id.txtTotalVendas);
                    TextView txtTotalLucro = findViewById(R.id.txtTotalLucro);

                    txtTotalVendas.setText(String.valueOf(totalVendas));
                    txtTotalQtd.setText(String.valueOf(totalQtd));

                    txtTotalLucro.setText(String.valueOf(df.format(totalLucro)));

                }

            }


        } else if (codBotao == 4) {

            String dataInicial = (String) bundle.getSerializable("DATAINICIAL");
            String dataFinal = (String) bundle.getSerializable("DATAFINAL");

            List<Vendas> dados = vendasRepositorio.buscarDataLista(dataInicial, dataFinal);

            if (dados.isEmpty()) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle("Aviso");
                dlg.setMessage("ESSA BUSCA NÃO RETORNOU NENHUM RESULTADO.");
                dlg.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent it = new Intent(dados_vendas.this, cons_vendas.class);
                        startActivity(it);
                    }
                });
                dlg.show();

            } else {
                vendasAdapter = new VendasAdapter(dados, conexao);
                lstDados.setAdapter(vendasAdapter);

                //Calcular totais
                Cursor resultado = conexao.rawQuery("SELECT COD_VENDA, QUANTIDADE, DATA, LOTE, ROTA FROM VENDAS WHERE strftime('%Y-%m-%d',DATA) BETWEEN '" + dataInicial + "' AND '" + dataFinal + "'", null);


                int totalVendas = resultado.getCount();
                int totalQtd = 0;
                Double totalLucro = Double.valueOf(0);

                if(resultado.getCount()>0 ){
                    resultado.moveToFirst();
                    do{
                        Vendas ven = new Vendas();
                        Estoque est = new Estoque();
                        ven.quantidade = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));

                        String[] parametros = new String[1];
                        parametros[0] = String.valueOf(resultado.getInt(resultado.getColumnIndexOrThrow("LOTE")));

                        Cursor resultadoEstoque = conexao.rawQuery("SELECT CUSTO, VENDA FROM ESTOQUE WHERE LOTE = ?" , parametros);
                        resultadoEstoque.moveToFirst();

                        est.custo = resultadoEstoque.getDouble(resultadoEstoque.getColumnIndexOrThrow("CUSTO")) * ven.quantidade;
                        est.venda = resultadoEstoque.getDouble(resultadoEstoque.getColumnIndexOrThrow("VENDA")) * ven.quantidade;

                        totalQtd = totalQtd + ven.quantidade;
                        totalLucro = totalLucro + (est.venda - est.custo);

                    }while(resultado.moveToNext());

                    TextView txtTotalQtd = findViewById(R.id.txtTotalQtdVendas);
                    TextView txtTotalVendas = findViewById(R.id.txtTotalVendas);
                    TextView txtTotalLucro = findViewById(R.id.txtTotalLucro);

                    txtTotalVendas.setText(String.valueOf(totalVendas));
                    txtTotalQtd.setText(String.valueOf(totalQtd));

                    txtTotalLucro.setText(String.valueOf(df.format(totalLucro)));

                }


            }


        }
    }

    public void criarConexao(){
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