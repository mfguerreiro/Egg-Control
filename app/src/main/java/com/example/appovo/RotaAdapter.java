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

import com.example.appovo.dominio.entidades.Rota;
import com.example.appovo.dominio.entidades.Tipo;

import java.util.List;

public class RotaAdapter extends RecyclerView.Adapter<RotaAdapter.ViewHolderRota> {
    private List<Rota> dados;

    public RotaAdapter(List<Rota> dados){
        this.dados = dados;
    }

    @NonNull
    @Override
    public RotaAdapter.ViewHolderRota onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.linha_rota, parent, false);
        ViewHolderRota holderRota = new ViewHolderRota(view, parent.getContext());
        return holderRota;
    }

    @Override
    public void onBindViewHolder(@NonNull RotaAdapter.ViewHolderRota holder, int position) {
        if((dados != null) && (dados.size() > 0)){
            Rota rota = dados.get(position);

            holder.txtCodRota.setText(Integer.toString(rota.cod_rota));
            holder.txtDescRota.setText(rota.descricao);
        }

    }

    @Override
    public int getItemCount() {
        return dados.size();
    }

    public class ViewHolderRota extends RecyclerView.ViewHolder{
        public TextView txtCodRota;
        public TextView txtDescRota;

        public ViewHolderRota(@NonNull View itemView, final Context context) {
            super(itemView);

            txtCodRota = (TextView) itemView.findViewById(R.id.txtCodRota);
            txtDescRota = (TextView) itemView.findViewById(R.id.txtDescRota);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dados.size() > 0){
                        Rota rota = dados.get(getLayoutPosition());
                        Intent it = new Intent(context, cad_rotas.class);
                        it.putExtra("ROTA", rota);

                        ((AppCompatActivity)context).startActivityForResult(it, 0);
                    }


                }
            });
        }
    }
}
