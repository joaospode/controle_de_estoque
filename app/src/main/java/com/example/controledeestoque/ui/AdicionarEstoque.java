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
import java.util.List;

public class AdicionarEstoque extends AppCompatActivity {
    private Spinner spUniformes;
    private EditText etQuantidade;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_estoque);

        db = new DatabaseHelper(this);
        spUniformes = findViewById(R.id.spinnerUniformes);
        etQuantidade = findViewById(R.id.etQuantidade);
        Button btn = findViewById(R.id.btnSalvarEstoque);

        // popula spinner de uniformes
        List<Uniforme> list = db.getAllUniformes();
        ArrayAdapter<Uniforme> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, list
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUniformes.setAdapter(adapter);

        btn.setOnClickListener(v -> {
            try {
                Uniforme u = (Uniforme) spUniformes.getSelectedItem();
                int q = Integer.parseInt(etQuantidade.getText().toString());
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
