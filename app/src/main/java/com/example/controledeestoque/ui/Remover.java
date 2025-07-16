package com.example.controledeestoque.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.controledeestoque.R;

public class Remover extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remover);

        Button btnRemFunc = findViewById(R.id.btnRemoveFuncionario);
        Button btnRemUni  = findViewById(R.id.btnRemoveUniforme);
        Button btnTrans   = findViewById(R.id.btnTransferUniformes);

        btnRemFunc.setOnClickListener(v ->
                startActivity(new Intent(Remover.this, RemoverFuncionario.class))
        );
        btnRemUni.setOnClickListener(v ->
                startActivity(new Intent(Remover.this, RemoverUniforme.class))
        );
        btnTrans.setOnClickListener(v ->
                startActivity(new Intent(Remover.this, RemoverEstoque.class))
        );
    }
}
