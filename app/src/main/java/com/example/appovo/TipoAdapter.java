package com.example.appovo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appovo.dominio.entidades.Tipo;
import com.example.appovo.dominio.entidades.Vendas;

import java.util.List;

public class TipoAdapter extends RecyclerView.Adapter<TipoAdapter.ViewHolderTipo> {
    private List<Tipo> dados;

    public TipoAdapter(List<Tipo> dados){
        this.dados = dados;
    }

    @NonNull
    @Override
    public TipoAdapter.ViewHolderTipo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.linha_tipo, parent, false);
        ViewHolderTipo holderTipo = new ViewHolderTipo(view, parent.getContext());
        return holderTipo;

    }

    @Override
    public void onBindViewHolder(@NonNull TipoAdapter.ViewHolderTipo holder, int position) {
        if((dados != null) && (dados.size() > 0)) {
            Tipo tipo = dados.get(position);

            holder.txtCodTipo.setText(Integer.toString(tipo.cod_tipo));
            holder.txtDescTipo.setText(tipo.descricao);
        }
    }

    @Override
    public int getItemCount() {
        return dados.size();
    }

    public class ViewHolderTipo extends RecyclerView.ViewHolder{
        public TextView txtCodTipo;
        public TextView txtDescTipo;

        public ViewHolderTipo(@NonNull View itemView, final Context context) {
            super(itemView);

            txtCodTipo = (TextView) itemView.findViewById(R.id.txtCodTipo);
            txtDescTipo = (TextView) itemView.findViewById(R.id.txtDescTipo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dados.size() > 0){
                        Tipo tipo = dados.get(getLayoutPosition());
                        Intent it = new Intent(context, cad_tipos.class);
                        it.putExtra("TIPO", tipo);

                        ((AppCompatActivity)context).startActivityForResult(it, 0);
                    }


                }
            });
        }
    }
}
