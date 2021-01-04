package com.example.appovo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.appovo.database.DadosOpenHelper;
import com.example.appovo.dominio.entidades.Estoque;
import com.example.appovo.dominio.entidades.Rota;
import com.example.appovo.dominio.entidades.Vendas;
import com.example.appovo.dominio.repositorio.EstoqueRepositorio;
import com.example.appovo.dominio.repositorio.RotaRepositorio;
import com.example.appovo.dominio.repositorio.VendasRepositorio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class cad_Vendas extends AppCompatActivity {
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private Vendas vendas;

    private VendasRepositorio vendasRepositorio;
    private EstoqueRepositorio estoqueRepositorio;
    private EstoqueAdapter estoqueAdapter;
    private RotaAdapter rotaAdapter;
    private RotaRepositorio rotaRepositorio;
    private Rota rota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad__vendas);
        final DecimalFormat df = new DecimalFormat("#,###.00"); //converter para apenas 2 casas decimais depois da virgula



        final TextView txtTipo = findViewById(R.id.txtTipo);
        final TextView txtQtdDisp = findViewById(R.id.txtQtdDisp);
        final TextView txtVenda = findViewById(R.id.txtVendas);

        final EditText edtLote = (EditText)findViewById(R.id.editTextLote);

        //código botão enviar
        Button btn_enviar = (Button) findViewById(R.id.btn_enviarVendas);

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirmar();
            }
        });

        //codigo data
        EditText dataatual = findViewById(R.id.editTextDate);
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        dataatual.setText(dateString);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        criarConexao();
        verificaParametro();

        //codigo para spinner rota

        Cursor resultado = conexao.rawQuery("SELECT DESCRICAO FROM ROTA", null);
        List dadosSpinner = new ArrayList();
        if(resultado.getCount()>0) {
            resultado.moveToFirst();
            do {
                    dadosSpinner.add(resultado.getString(resultado.getColumnIndexOrThrow("DESCRICAO")));
            } while (resultado.moveToNext());
        }

        Spinner rota = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter adapter_rota = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dadosSpinner);
        rota.setAdapter(adapter_rota);

        Cursor resultadoRota = conexao.rawQuery("SELECT ROTA FROM VENDAS ORDER BY COD_VENDA DESC LIMIT 1", null);

        if(resultadoRota.getCount()>0) {
            resultadoRota.moveToFirst();
            String ultimaRota = resultadoRota.getString(resultadoRota.getColumnIndexOrThrow("ROTA"));

            for (int i=0; i < rota.getCount(); i++){
                if (rota.getItemAtPosition(i).toString().equals(ultimaRota)){
                    rota.setSelection(i);
                }
            }
        }




        //botão lote
        Button btn_lote = (Button) findViewById(R.id.btn_lote);
        btn_lote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText edtNum = (EditText)findViewById(R.id.editTextNum); //entrada da quantidade
                String num = edtNum.getText().toString();

                if (num.isEmpty()){//verifica se quantidade foi digitada
                    AlertDialog.Builder dlg = new AlertDialog.Builder(cad_Vendas.this);
                    dlg.setTitle("Aviso");
                    dlg.setMessage("A QUANTIDADE NÃO FOI DIGITADA!");
                    dlg.setNeutralButton("Ok", null);
                    dlg.show();
                }else{
                    String lote = edtLote.getText().toString();

                    if (lote.isEmpty()){ //verifica se o lote foi digitado
                        edtLote.requestFocus();
                        AlertDialog.Builder dlg = new AlertDialog.Builder(cad_Vendas.this);
                        dlg.setTitle("Aviso");
                        dlg.setMessage("O LOTE NÃO FOI DIGITADO!");
                        dlg.setNeutralButton("Ok", null);
                        dlg.show();
                    }else{
                        estoqueRepositorio = new EstoqueRepositorio(conexao);
                        Estoque dados = estoqueRepositorio.buscarLote(Integer.parseInt(lote));

                        if (dados == null){
                            AlertDialog.Builder dlg = new AlertDialog.Builder(cad_Vendas.this);
                            dlg.setTitle("Aviso");
                            dlg.setMessage("LOTE INEXISTENTE!");
                            dlg.setNeutralButton("Ok", null);
                            dlg.show();
                        }else{
                            estoqueAdapter = new EstoqueAdapter(Collections.singletonList(dados));

                            RadioGroup venda = findViewById(R.id.radioGroup);
                            RadioButton cartela = findViewById(R.id.radioCartela);
                            RadioButton duzia = findViewById(R.id.radioDuzia);
                            RadioButton unidade = findViewById(R.id.radioUnidade);

                            if(venda.getCheckedRadioButtonId() == cartela.getId()){
                                vendas.quantidade = Integer.parseInt(num) * 30;
                                txtVenda.setText(df.format(dados.venda * 30));
                            }else if(venda.getCheckedRadioButtonId() == duzia.getId()){
                                vendas.quantidade = Integer.parseInt(num) * 12;
                                txtVenda.setText(df.format(dados.venda * 12));
                            }else if(venda.getCheckedRadioButtonId() == unidade.getId()){
                                vendas.quantidade = Integer.parseInt(num);
                                txtVenda.setText(df.format(dados.venda));
                            }
                            txtTipo.setText(dados.tipo);
                            txtQtdDisp.setText(String.valueOf(dados.quantidade));
                        }
                    }
                }
            }
        });
    }

    private void verificaParametro(){
        EditText edtNum = (EditText)findViewById(R.id.editTextNum);
        final EditText edtLote = (EditText)findViewById(R.id.editTextLote);
        EditText edtData = (EditText)findViewById(R.id.editTextDate);
        RadioButton unidade = findViewById(R.id.radioUnidade);

        Bundle bundle = getIntent().getExtras();

        vendas = new Vendas();

        if(bundle != null && bundle.containsKey("VENDAS")){
            vendas = (Vendas) bundle.getSerializable("VENDAS");

            edtNum.setText(String.valueOf(vendas.quantidade));
            edtLote.setText(String.valueOf(vendas.lote));
            edtData.setText(vendas.data);
            unidade.setChecked(true);

        }
    }

    private void criarConexao(){
        try {
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();

            vendasRepositorio = new VendasRepositorio(conexao);

        } catch(SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.lbl_ok, null);
            dlg.show();
        }
    }


        private void confirmar(){
            if(validaCampos() == false) {
                try {
                    //Código para consulta da quantidade em estoque
                    StringBuilder sql = new StringBuilder();
                    sql.append("SELECT QUANTIDADE");
                    sql.append("    FROM ESTOQUE");
                    sql.append("    WHERE LOTE = ?");

                    String[] parametrosConsulta = new String[1];
                    parametrosConsulta[0] = String.valueOf(vendas.lote);

                    Cursor resultado = conexao.rawQuery(sql.toString(), parametrosConsulta);
                    resultado.moveToFirst();
                    int qtdEstoque = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));

                    if (vendas.quantidade > qtdEstoque){
                        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                        dlg.setTitle("Aviso");
                        dlg.setMessage("A QUANTIDADE INFORMADA NÃO ESTÁ DISPONÍVEL NESSE LOTE.");
                        dlg.setNeutralButton("Ok", null);
                        dlg.show();
                    }else{
                        if (resultado.getCount() > 0) {
                            if (vendas.cod_vendas == 0) {
                                vendasRepositorio.inserir(vendas);

                                //código para alterar a quantidade em estoque
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("QUANTIDADE", qtdEstoque - vendas.quantidade); //fazer a consulta do quantidadeEstoque e diminuir esse valor

                                String[] parametros = new String[1];
                                parametros[0] = String.valueOf(vendas.lote);
                                conexao.update("ESTOQUE", contentValues, "LOTE = ?", parametros);
                            } else {
                                StringBuilder sqlVendas = new StringBuilder();
                                sqlVendas.append("SELECT QUANTIDADE");
                                sqlVendas.append("    FROM VENDAS");
                                sqlVendas.append("    WHERE COD_VENDA = ?");

                                String[] parametrosVendas = new String[1];
                                parametrosVendas[0] = String.valueOf(vendas.cod_vendas);

                                Cursor resultadoVendas = conexao.rawQuery(sqlVendas.toString(), parametrosVendas);
                                resultadoVendas.moveToFirst();
                                int qtdVendas = resultadoVendas.getInt(resultadoVendas.getColumnIndexOrThrow("QUANTIDADE"));

                                ContentValues contentValues = new ContentValues();
                                contentValues.put("QUANTIDADE", qtdEstoque + qtdVendas); //fazer a consulta do quantidadeEstoque e diminuir esse valor

                                String[] parametros = new String[1];
                                parametros[0] = String.valueOf(vendas.lote);
                                conexao.update("ESTOQUE", contentValues, "LOTE = ?", parametros);

                                vendasRepositorio.alterar(vendas);

                                //Código para consulta da quantidade em estoque
                                StringBuilder sqlNovoEstoque = new StringBuilder();
                                sqlNovoEstoque.append("SELECT QUANTIDADE");
                                sqlNovoEstoque.append("    FROM ESTOQUE");
                                sqlNovoEstoque.append("    WHERE LOTE = ?");

                                String[] parametrosEstoqueNovo = new String[1];
                                parametrosEstoqueNovo[0] = String.valueOf(vendas.lote);

                                Cursor resultadoEstoqueNovo = conexao.rawQuery(sqlNovoEstoque.toString(), parametrosEstoqueNovo);
                                if (resultadoEstoqueNovo.getCount() > 0) {
                                    resultadoEstoqueNovo.moveToFirst();
                                    int qtdEstoqueNovo = resultadoEstoqueNovo.getInt(resultadoEstoqueNovo.getColumnIndexOrThrow("QUANTIDADE"));

                                    //código para alterar a quantidade em estoque
                                    ContentValues contentValuesNovo = new ContentValues();
                                    contentValuesNovo.put("QUANTIDADE", qtdEstoqueNovo - vendas.quantidade); //fazer a consulta do quantidadeEstoque e diminuir esse valor


                                    conexao.update("ESTOQUE", contentValuesNovo, "LOTE = ?", parametrosEstoqueNovo);
                                }
                            }
                        }

                        //mensagem de sucesso
                        Toast toast = Toast.makeText(this, "Informações salvas com sucesso!", Toast.LENGTH_LONG);
                        toast.show();
                        finish();
                    }



                }catch(SQLException ex){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                    dlg.setTitle(R.string.erro);
                    dlg.setMessage(ex.getMessage());
                    dlg.setNeutralButton(R.string.lbl_ok, null);
                    dlg.show();
                }

            }
        }

    public void limpar(View view){
        EditText edtNum = findViewById(R.id.editTextNum);
        EditText edtLote = findViewById(R.id.editTextLote);

        edtNum.setText("");
        edtLote.setText("");

    }

    public void excluir(View view){
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle("Aviso");
        dlg.setMessage("Tem certeza que deseja excluir?");
        dlg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                vendasRepositorio.excluir(vendas.cod_vendas);

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT QUANTIDADE");
                sql.append("    FROM ESTOQUE");
                sql.append("    WHERE LOTE = ?");

                String[] parametrosConsulta = new String[1];
                parametrosConsulta[0] = String.valueOf(vendas.lote);

                Cursor resultado = conexao.rawQuery(sql.toString(), parametrosConsulta);
                if(resultado.getCount()>0) {
                    resultado.moveToFirst();
                    int qtdEstoque = resultado.getInt(resultado.getColumnIndexOrThrow("QUANTIDADE"));

                    //código para alterar a quantidade em estoque
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("QUANTIDADE", qtdEstoque + vendas.quantidade); //fazer a consulta do quantidadeEstoque e aumentar esse valor

                    String[] parametros = new String[1];
                    parametros[0] = String.valueOf(vendas.lote);

                    conexao.update("ESTOQUE", contentValues, "LOTE = ?", parametros);
                }

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

        private boolean validaCampos(){
            boolean res = false;

            EditText edtNum = (EditText)findViewById(R.id.editTextNum);
            EditText edtLote = (EditText)findViewById(R.id.editTextLote);
            EditText dataatual = findViewById(R.id.editTextDate);
            Spinner spinnerRota = findViewById(R.id.spinner2);

            String rota = String.valueOf(spinnerRota.getSelectedItem());
            String num = edtNum.getText().toString();
            String lote = edtLote.getText().toString();

            String data="ERRO!!!!!!!!!!!!!!!!!!!!!!";

            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                String dataInseridaInvert;
                Date dataInserida = new SimpleDateFormat("dd/MM/yyyy").parse(dataatual.getText().toString());
                dataInseridaInvert= (formatter.format(dataInserida)).replace("/", "-");
                data = dataInseridaInvert;
            } catch (ParseException e) {
                e.printStackTrace();
                dataatual.requestFocus();
            }



            if(res = isCampoVazio(num)){
                edtNum.requestFocus();
            }else if(res = isCampoVazio(lote)){
                edtLote.requestFocus();
            }else if(res = isCampoVazio(data)){
                dataatual.requestFocus();
            }

            if (res){
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle("Aviso");
                dlg.setMessage("Há campos em branco!");
                dlg.setNeutralButton("Ok",null);
                dlg.show();
            } else {
                RadioGroup venda = findViewById(R.id.radioGroup);
                RadioButton cartela = findViewById(R.id.radioCartela);
                RadioButton duzia = findViewById(R.id.radioDuzia);
                RadioButton unidade = findViewById(R.id.radioUnidade);

                if(venda.getCheckedRadioButtonId() == cartela.getId()){
                    vendas.quantidade = Integer.parseInt(num) * 30;
                }else if(venda.getCheckedRadioButtonId() == duzia.getId()){
                    vendas.quantidade = Integer.parseInt(num) * 12;
                }else if(venda.getCheckedRadioButtonId() == unidade.getId()){
                    vendas.quantidade = Integer.parseInt(num);
                }

                vendas.lote = Integer.parseInt(lote);

                vendas.data = data;
                vendas.rota = rota;
            }
            return res;
        }

        private boolean isCampoVazio(String valor){
            boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
            return resultado;
        }

}
