package com.example.appovo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appovo.dominio.entidades.Estoque;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EstoqueAdapter extends RecyclerView.Adapter<EstoqueAdapter.ViewHolderEstoque> {
    private List<Estoque> dados;

    public EstoqueAdapter (List<Estoque> dados){
        this.dados = dados;
    }

    @NonNull
    @Override
    public EstoqueAdapter.ViewHolderEstoque onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater LayoutInflater = android.view.LayoutInflater.from(parent.getContext());

        View view = LayoutInflater.inflate(R.layout.linha_estoque, parent, false);

        ViewHolderEstoque holderEstoque = new ViewHolderEstoque(view, parent.getContext());

        return holderEstoque;
    }

    public class ViewHolderEstoque extends RecyclerView.ViewHolder{
        public TextView txtLote;
        public TextView txtQtd;
        public TextView txtTipo;
        public TextView txtData;
        public TextView txtCusto;
        public TextView txtVenda;

        public ViewHolderEstoque(@NonNull View itemView, final Context context) {
            super(itemView);

            txtLote = (TextView) itemView.findViewById(R.id.txtLote);
            txtQtd  = (TextView) itemView.findViewById(R.id.txtQtd);
            txtTipo = (TextView) itemView.findViewById(R.id.txtTipo);
            txtData = (TextView) itemView.findViewById(R.id.txtData);
            txtCusto = (TextView) itemView.findViewById(R.id.txtQtdDisp);
            txtVenda = (TextView) itemView.findViewById(R.id.txtVenda);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(dados.size() > 0){
                        Estoque estoque = dados.get(getLayoutPosition());


                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date dataInserida = null;
                        try {
                            dataInserida = new SimpleDateFormat("yyyy-MM-dd").parse(estoque.data);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (estoque.data.contains("-") ){
                            estoque.data = (formatter.format(dataInserida)).replace("-", "/");
                        }
                        Intent it = new Intent(context, cad_estoque.class);
                        it.putExtra("ESTOQUE", estoque);
                        ((AppCompatActivity)context).startActivityForResult(it, 0);

                    }
                }
            });

        }
    }

    @Override
    public void onBindViewHolder(@NonNull EstoqueAdapter.ViewHolderEstoque holder, int position) {
        DecimalFormat df = new DecimalFormat("#,###.00"); //converter para apenas 2 casas decimais depois da virgula

        if ( (dados != null) && (dados.size() > 0) ){
            Estoque estoque = dados.get(position);

            holder.txtLote.setText(Integer.toString(estoque.lote));
            holder.txtQtd.setText(Double.toString(estoque.quantidade));
            holder.txtTipo.setText(estoque.tipo);

            //tratamento para exibir data no formato dd/MM/YYYY
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date dataInserida = null;
            try {
                dataInserida = new SimpleDateFormat("yyyy-MM-dd").parse(estoque.data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            estoque.data = (formatter.format(dataInserida)).replace("-", "/");

            holder.txtData.setText(estoque.data);

            holder.txtCusto.setText(df.format(estoque.custo));
            //holder.txtVenda.setText(Double.toString(estoque.venda)); venda não apresenta no relatório de estoque, apenas no de vendas

        }
    }

    @Override
    public int getItemCount() {
        return dados.size();
    }

}
