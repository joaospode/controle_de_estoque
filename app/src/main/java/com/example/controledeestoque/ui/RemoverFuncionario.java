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
import com.example.controledeestoque.model.Funcionario;

import java.util.List;

public class RemoverFuncionario extends AppCompatActivity {

    private Spinner spRemoverFuncionario;
    private Button btnConfirmar;
    private DatabaseHelper db;
    private List<Funcionario> funcionarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remover_funcionario);

        db = new DatabaseHelper(this);
        spRemoverFuncionario = findViewById(R.id.spRemoverFuncionario);
        btnConfirmar = findViewById(R.id.btnConfirmarRemocaoFuncionario);

        // Carrega lista de funcionários
        funcionarios = db.getAllFuncionarios();
        ArrayAdapter<Funcionario> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                funcionarios
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRemoverFuncionario.setAdapter(adapter);

        btnConfirmar.setOnClickListener(v -> {
            final Funcionario f = (Funcionario) spRemoverFuncionario.getSelectedItem();
            if (f == null) {
                Toast.makeText(this, "Nenhum funcionário selecionado.", Toast.LENGTH_SHORT).show();
                return;
            }
            new AlertDialog.Builder(this)
                    .setTitle("Confirmar remoção")
                    .setMessage("Deseja realmente remover '" + f.getNome() + "'?" )
                    .setPositiveButton("Sim", (dialog, which) -> {
                        int rows = db.deleteFuncionarioById(f.getId());
                        String msg = rows > 0 ? "Funcionário removido." : "Falha ao remover.";
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        if (rows > 0) finish();
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });
    }
}