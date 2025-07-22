package com.example.controledeestoque.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.Funcionario;

import java.util.ArrayList;
import java.util.List;

public class ListaFuncionario extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView rvFuncionarios;
    private FuncionarioAdapter adapter;
    private TextView tvEmpty;

    private EditText etNome, etSetor, etData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_funcionario);

        dbHelper = new DatabaseHelper(this);
        rvFuncionarios = findViewById(R.id.rvFuncionarios);
        tvEmpty = findViewById(R.id.tvEmptyFuncionarios);

        etNome = findViewById(R.id.etFiltroNome);
        etSetor = findViewById(R.id.etFiltroSetor);
        etData = findViewById(R.id.etFiltroData);

        rvFuncionarios.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FuncionarioAdapter(new ArrayList<>());
        rvFuncionarios.setAdapter(adapter);

        TextWatcher watcher = new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aplicarFiltro(
                        etNome.getText().toString(),
                        etSetor.getText().toString(),
                        etData.getText().toString()
                );
            }
        };

        etNome.addTextChangedListener(watcher);
        etSetor.addTextChangedListener(watcher);
        etData.addTextChangedListener(watcher);

        aplicarFiltro("", "", "");
    }

    private void aplicarFiltro(String nome, String setor, String data) {
        List<Funcionario> todos = dbHelper.getAllFuncionarios();
        List<Funcionario> filtrados = new ArrayList<>();

        for (Funcionario f : todos) {
            boolean matchNome = f.getNome().toLowerCase().contains(nome.toLowerCase());
            boolean matchSetor = f.getSetor().toLowerCase().contains(setor.toLowerCase());
            boolean matchData = f.getDataAdmissao().contains(data);

            if (matchNome && matchSetor && matchData) {
                filtrados.add(f);
            }
        }

        if (filtrados.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvFuncionarios.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvFuncionarios.setVisibility(View.VISIBLE);
        }

        adapter.updateList(filtrados);
    }

    private abstract static class TextWatcherAdapter implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}
    }
}