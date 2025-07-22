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

import java.util.List;

public class AdicionarSetorFuncao extends AppCompatActivity {

    private Spinner spinnerSetores;
    private EditText etNovoSetor, etFuncao;
    private Button btnSalvar;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_setor_funcao);

        db = new DatabaseHelper(this);

        spinnerSetores = findViewById(R.id.spinnerSetores);
        etNovoSetor     = findViewById(R.id.etNovoSetor);
        etFuncao        = findViewById(R.id.etFuncao);
        btnSalvar       = findViewById(R.id.btnSalvarSetorFuncao);

        carregarSetores();

        btnSalvar.setOnClickListener(v -> salvarFuncaoNoSetor());
    }

    private void carregarSetores() {
        List<String> setores = db.getAllSetores();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                setores
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSetores.setAdapter(adapter);
    }

    private void salvarFuncaoNoSetor() {
        String setorSelecionado = spinnerSetores.getSelectedItem() != null
                ? spinnerSetores.getSelectedItem().toString() : null;
        String novoSetor = etNovoSetor.getText().toString().trim();
        String funcao    = etFuncao.getText().toString().trim();

        if (funcao.isEmpty()) {
            Toast.makeText(this, "Digite o nome da função.", Toast.LENGTH_SHORT).show();
            return;
        }

        String setorFinal = novoSetor.isEmpty() ? setorSelecionado : novoSetor;

        if (setorFinal == null || setorFinal.isEmpty()) {
            Toast.makeText(this, "Selecione ou insira um setor.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!novoSetor.isEmpty()) {
            db.insertSetor(novoSetor);  // adiciona novo setor, se houver
            carregarSetores(); // atualiza spinner
        }

        long setorId = db.getSetorIdByNome(setorFinal);  // ✔ correto: setorId é long
        if (setorId == -1) {
            Toast.makeText(this, "Erro ao localizar o setor.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean ok = db.insertFuncao(funcao, (int) setorId);
        if (ok) {
            Toast.makeText(this, "Função adicionada ao setor.", Toast.LENGTH_SHORT).show();
            etFuncao.setText("");
        } else {
            Toast.makeText(this, "Erro ao salvar função.", Toast.LENGTH_SHORT).show();
        }
    }
}