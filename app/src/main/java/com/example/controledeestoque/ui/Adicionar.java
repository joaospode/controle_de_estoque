package com.example.controledeestoque.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.controledeestoque.R;

public class Adicionar extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar);

        Button btnFuncionario = findViewById(R.id.btnAddFuncionario);
        Button btnUniforme    = findViewById(R.id.btnAddUniforme);

        btnFuncionario.setOnClickListener(v ->
                startActivity(new Intent(this, AdicionarFuncionario.class))
        );
        btnUniforme.setOnClickListener(v ->
                startActivity(new Intent(this, AdicionarUniforme.class))
        );

        findViewById(R.id.btnAddEstoque).setOnClickListener(v ->
                startActivity(new Intent(Adicionar.this, AdicionarEstoque.class))
        );
    }
}
