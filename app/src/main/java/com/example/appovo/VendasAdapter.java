package com.example.appovo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appovo.dominio.entidades.Vendas;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VendasAdapter extends RecyclerView.Adapter<VendasAdapter.ViewHolderVendas>{
    private final SQLiteDatabase conexao;
    public int lote;
    private Context context;

    public double totalCusto = 0;

    dados_estoque de = new dados_estoque();

    private List<Vendas> dados;

    public VendasAdapter(List<Vendas> dados, SQLiteDatabase conexao){
        this.conexao = conexao;
        this.dados = dados;
    }

    @NonNull
    @Override
    public VendasAdapter.ViewHolderVendas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater LayoutInflater = android.view.LayoutInflater.from(parent.getContext());

        View view = LayoutInflater.inflate(R.layout.linha_vendas, parent, false);

        ViewHolderVendas holderVendas = new ViewHolderVendas(view, parent.getContext());

        return holderVendas;
    }

    @Override
    public void onBindViewHolder(@NonNull VendasAdapter.ViewHolderVendas holder, int position) {
        DecimalFormat df = new DecimalFormat("#,###.00"); //converter para apenas 2 casas decimais depois da virgula
        this.context = context;
        if ( (dados != null) && (dados.size() > 0) ){
            Vendas vendas = dados.get(position);

            holder.txtLote.setText(Integer.toString(vendas.lote));

            holder.txtQtd.setText(Double.toString(vendas.quantidade));

            //tratamento para exibir data no formato dd/MM/YYYY
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date dataInserida = null;
            try {
                dataInserida = new SimpleDateFormat("yyyy-MM-dd").parse(vendas.data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (vendas.data.contains("-")){
                vendas.data = (formatter.format(dataInserida)).replace("-", "/");
            }

            holder.txtData.setText(vendas.data);

            holder.txtCod.setText(Double.toString(vendas.cod_vendas));
            holder.txtRota.setText(vendas.rota);

            //Código para consulta da quantidade em estoque
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT TIPO, CUSTO, VENDA");
            sql.append("    FROM ESTOQUE");
            sql.append("    WHERE LOTE = ?");

            String[] parametrosConsulta = new String[1];
            parametrosConsulta[0] = String.valueOf(vendas.lote);

            Cursor resultado = conexao.rawQuery(sql.toString(), parametrosConsulta);
            if(resultado.getCount()>0) {
                resultado.moveToFirst();

                double custoReal = resultado.getDouble(resultado.getColumnIndexOrThrow("CUSTO"));
                double vendaReal = resultado.getDouble(resultado.getColumnIndexOrThrow("VENDA"));

                holder.txtCusto.setText(String.valueOf(df.format(custoReal * vendas.quantidade)));
                holder.txtVenda.setText(String.valueOf(df.format(vendaReal * vendas.quantidade)));
                holder.txtTipo.setText(resultado.getString(resultado.getColumnIndexOrThrow("TIPO")));

                totalCusto += (custoReal * vendas.quantidade);
            }
        }
    }

    public int getItemCount() {
        return dados.size();
    }

    public class ViewHolderVendas extends RecyclerView.ViewHolder{

        public TextView txtCod;
        public TextView txtLote;
        public TextView txtQtd;
        public TextView txtData;
        public TextView txtCusto;
        public TextView txtRota;
        public TextView txtTipo;
        public TextView txtVenda;

        public ViewHolderVendas(@NonNull View itemView, final Context context) {
            super(itemView);

            txtCod = (TextView) itemView.findViewById(R.id.txtCod);
            txtLote = (TextView) itemView.findViewById(R.id.txtLote);
            txtQtd  = (TextView) itemView.findViewById(R.id.txtQtd);
            txtTipo = (TextView) itemView.findViewById(R.id.txtTipo);
            txtData = (TextView) itemView.findViewById(R.id.txtData);
            txtRota = (TextView) itemView.findViewById(R.id.txtRota);
            txtCusto = (TextView) itemView.findViewById(R.id.txtQtdDisp);
            txtVenda = (TextView) itemView.findViewById(R.id.txtVenda);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dados.size() > 0){
                        Vendas vendas = dados.get(getLayoutPosition());

                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date dataInserida = null;
                        try {
                            dataInserida = new SimpleDateFormat("yyyy-MM-dd").parse(vendas.data);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (vendas.data.contains("-")){
                            vendas.data = (formatter.format(dataInserida)).replace("-", "/");
                        }

                        Intent it = new Intent(context, cad_Vendas.class);
                        it.putExtra("VENDAS", vendas);

                        ((AppCompatActivity)context).startActivityForResult(it, 0);
                    }


                }
            });
        }
    }
}
