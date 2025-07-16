package com.example.controledeestoque.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controledeestoque.R;


public class Main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnAdd).setOnClickListener(v ->
                startActivity(new Intent(this, Adicionar.class)));
        findViewById(R.id.btnRemove).setOnClickListener(v ->
                startActivity(new Intent(this, Remover.class)));
        findViewById(R.id.btnDeliver).setOnClickListener(v ->
                startActivity(new Intent(this, Entregar.class)));
        findViewById(R.id.btnHistory).setOnClickListener(v ->
                startActivity(new Intent(this, Historico.class)));
        findViewById(R.id.btnSettings).setOnClickListener(v ->
                startActivity(new Intent(this, Configuracoes.class)));

    }
}