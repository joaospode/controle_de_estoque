package com.example.controledeestoque.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.Uniforme;

import java.util.ArrayList;
import java.util.List;

public class AdicionarEstoque extends AppCompatActivity {
    private Spinner spUniformes;
    private EditText etQuantidade;
    private DatabaseHelper db;
    private List<Uniforme> uniformes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_estoque);

        db = new DatabaseHelper(this);
        spUniformes = findViewById(R.id.spinnerUniformes);
        etQuantidade = findViewById(R.id.etQuantidade);
        Button btnSalvar = findViewById(R.id.btnSalvarEstoque);

        // Carrega a lista de uniformes e seus tipos para o Spinner
        uniformes = db.getAllUniformes();
        List<String> tipos = new ArrayList<>();
        for (Uniforme u : uniformes) {
            tipos.add(u.getTipo());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                tipos
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUniformes.setAdapter(adapter);

        btnSalvar.setOnClickListener(v -> {
            try {
                int pos = spUniformes.getSelectedItemPosition();
                Uniforme u = uniformes.get(pos);
                int q = Integer.parseInt(etQuantidade.getText().toString().trim());

                boolean ok = db.addStock(u.getId(), q);
                Toast.makeText(
                        this,
                        ok ? "Estoque atualizado." : "Falha ao atualizar estoque.",
                        Toast.LENGTH_SHORT
                ).show();
                if (ok) finish();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Informe quantidade válida.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
