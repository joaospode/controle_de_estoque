package com.example.controledeestoque.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.Uniforme;

import java.util.List;

public class RemoverUniforme extends AppCompatActivity {

    private Spinner spRemoverUniforme;
    private Button btnConfirmar;
    private DatabaseHelper db;
    private List<Uniforme> uniformes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remover_uniforme);

        db = new DatabaseHelper(this);
        spRemoverUniforme = findViewById(R.id.spRemoverUniforme);
        btnConfirmar = findViewById(R.id.btnConfirmarRemocaoUniforme);

        // Carrega lista de uniformes
        uniformes = db.getAllUniformes();
        ArrayAdapter<Uniforme> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                uniformes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRemoverUniforme.setAdapter(adapter);

        btnConfirmar.setOnClickListener(v -> {
            final Uniforme u = (Uniforme) spRemoverUniforme.getSelectedItem();
            if (u == null) {
                Toast.makeText(this, "Nenhum uniforme selecionado.", Toast.LENGTH_SHORT).show();
                return;
            }
            new AlertDialog.Builder(this)
                    .setTitle("Confirmar remoção")
                    .setMessage("Deseja realmente remover '" + u.getTipo() + "'?" )
                    .setPositiveButton("Sim", (dialog, which) -> {
                        int rows = db.deleteUniformeById(u.getId());
                        String msg = rows > 0 ? "Uniforme removido." : "Falha ao remover.";
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        if (rows > 0) finish();
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });
    }
}