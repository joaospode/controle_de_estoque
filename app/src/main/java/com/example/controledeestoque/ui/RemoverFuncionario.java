package com.example.controledeestoque.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;

public class RemoverFuncionario extends AppCompatActivity {
    private EditText etCpf;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_remover_funcionario);
        db    = new DatabaseHelper(this);
        etCpf = findViewById(R.id.etCpfRemove);
        Button btn = findViewById(R.id.btnRemoveFunc);
        btn.setOnClickListener(v -> {
            String cpf = etCpf.getText().toString().trim();
            if (TextUtils.isEmpty(cpf)) {
                Toast.makeText(this, "Informe o CPF.", Toast.LENGTH_SHORT).show();
                return;
            }
            int removed = db.deleteFuncionarioByCpf(cpf);
            Toast.makeText(this,
                    removed>0? "Funcionário removido." : "Nenhum registro removido.",
                    Toast.LENGTH_SHORT).show();
            if (removed>0) finish();
        });
    }
}
