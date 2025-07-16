package com.example.controledeestoque.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;

public class RemoverUniforme extends AppCompatActivity {
    private EditText etTipo;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_remover_uniforme);
        db     = new DatabaseHelper(this);
        etTipo = findViewById(R.id.etTipoRemove);
        Button btn = findViewById(R.id.btnRemoveUni);
        btn.setOnClickListener(v -> {
            String tipo = etTipo.getText().toString().trim();
            if (TextUtils.isEmpty(tipo)) {
                Toast.makeText(this, "Informe o tipo.", Toast.LENGTH_SHORT).show();
                return;
            }
            int removed = db.deleteUniformeByTipo(tipo);
            Toast.makeText(this,
                    removed>0? "Uniforme removido." : "Nenhum registro removido.",
                    Toast.LENGTH_SHORT).show();
            if (removed>0) finish();
        });
    }
}