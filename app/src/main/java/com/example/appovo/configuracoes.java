package com.example.appovo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class configuracoes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        getSupportActionBar().hide();//remover titlebar

        Button btnTipos = findViewById(R.id.btn_tipos);
        Button btnRotas = findViewById(R.id.btn_rotas);

        btnTipos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(configuracoes.this, tipos.class);
                startActivity(it);
            }
        });

        btnRotas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(configuracoes.this, rotas.class);
                startActivity(it);
            }
        });
    }
}