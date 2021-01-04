package com.example.appovo;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class relatorios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorios);
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
        //botão estoque
        Button btn_estoque = (Button) findViewById(R.id.btn_estoque);

        btn_estoque.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(relatorios.this, cons_estoque.class);
                startActivity(it);
            }
        });
        //botão vendas
        Button btn_vendas = (Button) findViewById(R.id.btn_vendas);

        btn_vendas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(relatorios.this, cons_vendas.class);
                startActivity(it);
            }
        });
    }
}