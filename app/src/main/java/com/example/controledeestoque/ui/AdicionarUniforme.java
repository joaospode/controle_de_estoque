package com.example.controledeestoque.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.Uniforme;

public class AdicionarUniforme extends AppCompatActivity {
    private EditText etTipo, etCa;
    private Button btnSalvar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_uniforme);

        dbHelper = new DatabaseHelper(this);
        etTipo   = findViewById(R.id.etTipo);
        etCa     = findViewById(R.id.etCa);
        btnSalvar= findViewById(R.id.btnSalvarUniforme);

        btnSalvar.setOnClickListener(v -> salvarUniforme());
    }

    private void salvarUniforme() {
        String tipo = etTipo.getText().toString().trim();
        String ca   = etCa.getText().toString().trim();

        if (TextUtils.isEmpty(tipo)) {
            Toast.makeText(this, "O tipo é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }

        Uniforme u = new Uniforme();
        u.setTipo(tipo);
        u.setCa(Integer.parseInt(ca));

        long id = dbHelper.insertUniforme(u);
        if (id > 0) {
            Toast.makeText(this, "Uniforme salvo (ID=" + id + ").", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar uniforme.", Toast.LENGTH_LONG).show();
        }
    }
}
