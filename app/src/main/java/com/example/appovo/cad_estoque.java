package com.example.appovo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
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

import com.example.appovo.database.DadosOpenHelper;
import com.example.appovo.dominio.entidades.Estoque;
import com.example.appovo.dominio.repositorio.EstoqueRepositorio;
import com.example.appovo.dominio.repositorio.RotaRepositorio;
import com.example.appovo.dominio.repositorio.TipoRepositorio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class cad_estoque extends AppCompatActivity {

    private SQLiteDatabase conexao; //conexão com banco
    private DadosOpenHelper dadosOpenHelper;
    private TipoRepositorio tipoRepositorio;

    private EstoqueRepositorio estoqueRepositorio;

    public Estoque est = new Estoque();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_estoque);
        getSupportActionBar().hide();//remover titlebar

        //codigo data
        EditText edtDataatual = findViewById(R.id.editTextDate);

        Calendar calendar = new GregorianCalendar();

        int diaIni = calendar.get(Calendar.DAY_OF_MONTH);
        int mesIni = calendar.get(Calendar.MONTH);
        int anoIni = calendar.get(Calendar.YEAR);

        String dataAtual = (diaIni) + "/" + (mesIni) + "/" + (anoIni);
        final String dataAtualBanco = String.valueOf((anoIni) + mesIni + diaIni);
        edtDataatual.setText(dataAtual);

        //conexao com banco de dados
        criarConexao();
        verificaParametro();

        //codigo sppiner tipo
        Cursor resultado = conexao.rawQuery("SELECT DESCRICAO FROM TIPO", null);
        List dadosSpinner = new ArrayList();
        if(resultado.getCount()>0) {
            resultado.moveToFirst();
            do {
                dadosSpinner.add(resultado.getString(resultado.getColumnIndexOrThrow("DESCRICAO")));
            } while (resultado.moveToNext());
        }
        Spinner rota = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter adapter_rota = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dadosSpinner);
        rota.setAdapter(adapter_rota);
    }

    private void verificaParametro(){
        EditText edtNroCaixas = findViewById(R.id.num_caixas);
        EditText edtCusto = findViewById(R.id.valor_custo);
        EditText edtVenda = findViewById(R.id.valor_venda);
        EditText edtData = findViewById(R.id.editTextDate);
        RadioButton radioUnidadeEstoque = findViewById(R.id.radioUnidadeEstoque);


        Bundle bundle = getIntent().getExtras();

        est = new Estoque();

        if ((bundle != null) && (bundle.containsKey("ESTOQUE"))){
            est = (Estoque) bundle.getSerializable("ESTOQUE");

            edtNroCaixas.setText(Integer.toString(est.quantidade));
            edtCusto.setText(Double.toString(est.custo));
            edtVenda.setText(Double.toString(est.venda));
            edtData.setText(est.data);
            radioUnidadeEstoque.setChecked(true);

        }


    }

    private void criarConexao(){
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

    private boolean validaCampos(){
        boolean res = false;

        EditText edtNroCaixas = findViewById(R.id.num_caixas);
        EditText edtCusto = findViewById(R.id.valor_custo);
        EditText edtVenda = findViewById(R.id.valor_venda);
        EditText edtData = findViewById(R.id.editTextDate);
        Spinner spiTipo = findViewById(R.id.spinner1);

        String nro_caixas = edtNroCaixas.getText().toString();
        String custo = edtCusto.getText().toString();
        String venda = edtVenda.getText().toString();

        String tipo = spiTipo.getSelectedItem().toString();

        String data="ERRO!!!!!!!!!!!!!!!!!!!!!!";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            String dataInseridaInvert;
            Date dataInserida = new SimpleDateFormat("dd/MM/yyyy").parse(edtData.getText().toString());
            dataInseridaInvert= (formatter.format(dataInserida)).replace("/", "-");
            data = dataInseridaInvert;
        } catch (ParseException e) {
            e.printStackTrace();
            edtData.requestFocus();
        }



        if(res = isCampoVazio(nro_caixas)){
            edtNroCaixas.requestFocus();
        }else if (res = isCampoVazio(custo)){
            edtCusto.requestFocus();
        }else if(res = isCampoVazio(venda)){
            edtVenda.requestFocus();
        }else if(res = isCampoVazio(data)){
            edtData.requestFocus();
        }

        if (res){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Aviso");
            dlg.setMessage("Há campos em branco!");
            dlg.setNeutralButton("Ok",null);
            dlg.show();
        } else {
            RadioGroup radioGroupEstoque = findViewById(R.id.radioGroupEstoque);
            RadioButton radioUnidadeEstoque = findViewById(R.id.radioUnidadeEstoque);
            RadioButton radioCartelaEstoque = findViewById(R.id.radioCartelaEstoque);
            RadioButton radioCaixaEstoque = findViewById(R.id.radioCaixaEstoque);

            if(radioGroupEstoque.getCheckedRadioButtonId() == radioUnidadeEstoque.getId()){
                est.quantidade = Integer.parseInt(nro_caixas);
                est.custo = Double.parseDouble(custo);
                est.venda = Double.parseDouble(venda);
            }else if(radioGroupEstoque.getCheckedRadioButtonId() == radioCartelaEstoque.getId()){
                est.quantidade = Integer.parseInt(nro_caixas) * 30;
                est.custo = Double.parseDouble(custo) / 30;
                est.venda = Double.parseDouble(venda) / 30;
            }else if(radioGroupEstoque.getCheckedRadioButtonId() == radioCaixaEstoque.getId()){
                est.quantidade = Integer.parseInt(nro_caixas) * 360;
                est.custo = Double.parseDouble(custo) / 360;
                est.venda = Double.parseDouble(venda) / 360;
            }

            est.data = (data);
            est.tipo = (tipo);
        }

        return res;
    }

    private boolean isCampoVazio(String valor){
        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;
    }


    public void confirmar(View view) {

        if(validaCampos() == false){
            try {
                if (est.lote == 0){
                    estoqueRepositorio.inserir(est);
                }else{
                    estoqueRepositorio.alterar(est);
                }
                Toast toast = Toast.makeText(this, "Informações salvas com sucesso!", Toast.LENGTH_LONG);
                toast.show();
                finish();
            } catch(SQLException ex){
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle(R.string.erro);
                dlg.setMessage(ex.getMessage());
                dlg.setNeutralButton(R.string.lbl_ok, null);
                dlg.show();
            }
        }
    }

    public void limpar(View view){
        EditText edtNroCaixas = findViewById(R.id.num_caixas);
        EditText edtCusto = findViewById(R.id.valor_custo);
        EditText edtVenda = findViewById(R.id.valor_venda);
        EditText edtData = findViewById(R.id.editTextDate);
        Spinner spiTipo = findViewById(R.id.spinner1);

        edtNroCaixas.setText("");
        edtCusto.setText("");
        edtVenda.setText("");
    }

    public void excluir(View view){
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle("Aviso");
        dlg.setMessage("Tem certeza que deseja excluir?");
        dlg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                estoqueRepositorio.excluir(est.lote);
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