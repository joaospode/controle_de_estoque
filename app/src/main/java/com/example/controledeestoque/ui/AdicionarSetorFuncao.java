package com.example.controledeestoque.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;

public class AdicionarSetorFuncao{
    private EditText etSetor, etFuncao;
    private Button btnSalvar;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_setor_funcao);

        etSetor = findViewById(R.id.etSetor);
        etFuncao = findViewById(R.id.etFuncao);
        btnSalvar = findViewById(R.id.btnSalvarSetorFuncao);

        db = new DatabaseHelper(this);

        btnSalvar.setOnClickListener(v -> salvar());
    }

    private void salvar() {
        String setor = etSetor.getText().toString().trim();
        String funcao = etFuncao.getText().toString().trim();

        if (TextUtils.isEmpty(setor) || TextUtils.isEmpty(funcao)) {
            Toast.makeText(this, "Preencha os dois campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        long setorId = db.insertSetor(setor);
        if (setorId == -1) {
            Toast.makeText(this, "Erro ao inserir setor. Pode já existir.", Toast.LENGTH_SHORT).show();
            return;
        }

        long funcaoId = db.insertFuncao(funcao, (int) setorId);
        if (funcaoId > 0) {
            Toast.makeText(this, "Setor e função cadastrados.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao inserir função.", Toast.LENGTH_SHORT).show();
        }
    }
}
