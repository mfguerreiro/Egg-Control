package com.example.appovo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appovo.database.DadosOpenHelper;
import com.example.appovo.dominio.repositorio.EstoqueRepositorio;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class cons_estoque extends AppCompatActivity {
    private SQLiteDatabase conexao; //conexão com banco
    private DadosOpenHelper dadosOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cons_estoque);
        getSupportActionBar().hide();//remover titlebar

        criarConexao();

        //BOTÃO CONSULTAR
        Button btn_consultar = (Button) findViewById(R.id.btn_consultar);
        btn_consultar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(cons_estoque.this, dados_estoque.class);
                it.putExtra("CODIGOBOTAO", 1);
                startActivity(it);
            }
        });

        //EDITTEXT LOTE
        final EditText edtLote = findViewById(R.id.edtLote);

        //BOTÃO CONSULTAR LOTE
        Button btn_lote = findViewById(R.id.btn_ConsultarLote);
        final AlertDialog.Builder dlg = new AlertDialog.Builder(this);

            btn_lote.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (edtLote.getText().length() == 0){
                        edtLote.requestFocus();
                        dlg.setTitle("Aviso");
                        dlg.setMessage("Ops! Você não digitou nenhum lote.");
                        dlg.setNeutralButton("Ok",null);
                        dlg.show();
                    }else{
                        Intent it = new Intent(cons_estoque.this, dados_estoque.class);
                        it.putExtra("LOTE", Integer.parseInt(String.valueOf(edtLote.getText())));
                        it.putExtra("CODIGOBOTAO", 2);
                        startActivity(it);
                    }

            }
        });

        //codigo sppiner tipo
        Cursor resultado = conexao.rawQuery("SELECT DESCRICAO FROM TIPO", null);
        List dadosSpinner = new ArrayList();
        if(resultado.getCount()>0) {
            resultado.moveToFirst();
            do {
                dadosSpinner.add(resultado.getString(resultado.getColumnIndexOrThrow("DESCRICAO")));
            } while (resultado.moveToNext());
        }
        final Spinner rota = (Spinner) findViewById(R.id.spinnerTipoEstoque);
        ArrayAdapter adapter_rota = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dadosSpinner);
        rota.setAdapter(adapter_rota);

        //BOTÃO CONSULTAR TIPO
        Button btn_tipo = findViewById(R.id.btn_ConsultarTipo);
        btn_tipo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(cons_estoque.this, dados_estoque.class);
                it.putExtra("TIPO", String.valueOf(rota.getSelectedItem()));
                it.putExtra("CODIGOBOTAO", 3);
                startActivity(it);
            }
        });

        //codigo data inicial
        final EditText edtDataInicial = findViewById(R.id.editTextDataInicial);
        Calendar calendar = new GregorianCalendar();

        int diaIni = calendar.get(Calendar.DAY_OF_MONTH);
        int mesIni = calendar.get(Calendar.MONTH);
        int anoIni = calendar.get(Calendar.YEAR);

        final String dataInicial = (diaIni) + "/" + (mesIni) + "/" + (anoIni);
        edtDataInicial.setText(dataInicial);

            //inverter data final p/ inserir no banco
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            String dataInicialInvert="ERRO!!!!!!!!!!!!!!!!!!!!!!";
            try {
                Date dataInserida = new SimpleDateFormat("dd/MM/yyyy").parse(String.valueOf(edtDataInicial.getText()));
                dataInicialInvert= (formatter.format(dataInserida)).replace("/", "-");
            } catch (ParseException e) {
                e.printStackTrace();
            }

        //codigo data final
        final EditText edtDataFinal = findViewById(R.id.editTextDataFinal);
        int diaFin = calendar.get(Calendar.DAY_OF_MONTH);
        int mesFin = calendar.get(Calendar.MONTH) + 1;
        int anoFin = calendar.get(Calendar.YEAR);

        final String dataFinal = (diaFin) + "/" + (mesFin) + "/" + (anoFin);
        edtDataFinal.setText(dataFinal);



        //BOTÃO CONSULTAR DATA
        Button btn_data = findViewById(R.id.btn_ConsultarData);

        btn_data.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (edtDataInicial.getText().length() == 0 || edtDataFinal.getText().length() == 0){   //testar se está vazio
                    if (edtDataInicial.getText().length() == 0){
                        edtDataInicial.requestFocus();
                    }else if (edtDataFinal.getText().length() == 0){
                        edtDataFinal.requestFocus();
                    }

                    dlg.setTitle("Aviso");
                    dlg.setMessage("Digite as duas datas para fazer uma consulta.");
                    dlg.setNeutralButton("Ok",null);
                    dlg.show();
                } else if (!verificarData(String.valueOf(edtDataInicial.getText())) || !verificarData(String.valueOf(edtDataFinal.getText()))){  //testar se está no formato de data

                    if (!verificarData(String.valueOf(edtDataInicial.getText()))){
                        edtDataInicial.requestFocus();
                    }else if (!verificarData(String.valueOf(edtDataFinal.getText()))){
                        edtDataFinal.requestFocus();
                    }

                    dlg.setTitle("Aviso");
                    dlg.setMessage("Uma das datas está com o formato errado. As datas devem respeitar o formato: DD/MM/AAAA");
                    dlg.setNeutralButton("Ok",null);
                    dlg.show();

                }

                else{
                    //inverter data final p/ inserir no banco e atualizar valor passado para proxima tela
                    String dataFinalInvert="ERRO!!!!!!!!!!!!!!!!!!!!!!";
                    try {
                        Date dataInserida = new SimpleDateFormat("dd/MM/yyyy").parse(String.valueOf(edtDataFinal.getText()));
                        dataFinalInvert= (formatter.format(dataInserida)).replace("/", "-");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    final String finalDataFinalInvert = dataFinalInvert;

                    //inverter data final p/ inserir no banco e atualizar valor passado para proxima tela
                    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                    String dataInicialInvert="ERRO!!!!!!!!!!!!!!!!!!!!!!";
                    try {
                        Date dataInserida = new SimpleDateFormat("dd/MM/yyyy").parse(String.valueOf(edtDataInicial.getText()));
                        dataInicialInvert= (formatter.format(dataInserida)).replace("/", "-");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    final String finalDataInicialInvert = dataInicialInvert;


                    Intent it = new Intent(cons_estoque.this, dados_estoque.class);
                    it.putExtra("DATAINICIAL", finalDataInicialInvert);
                    it.putExtra("DATAFINAL", finalDataFinalInvert);
                    it.putExtra("CODIGOBOTAO", 4);
                    startActivity(it);
                }

            }
        });
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

    public boolean verificarData(String data) {
        try {
            //SimpleDateFormat é usada para trabalhar com formatação de datas
            //neste caso meu formatador irá trabalhar com o formato "dd/MM/yyyy"
            //dd = dia, MM = mes, yyyy = ano
            //o "M" dessa String é maiusculo porque "m" minusculo se n me engano é minutos
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //a mágica desse método acontece aqui, pois o setLenient() é usado para setar
            //sua escolha sobre datas estranhas, quando eu seto para "false" estou dizendo
            //que não aceito datas falsas como 31/02/2016
            sdf.setLenient(false);
            //aqui eu tento converter a String em um objeto do tipo date, se funcionar
            //sua data é valida
            sdf.parse(data);
            //se nada deu errado retorna true (verdadeiro)
            return true;
        } catch (ParseException ex) {
            //se algum passo dentro do "try" der errado quer dizer que sua data é falsa, então,
            //retorna falso
            return false;
        }
    }


    }
