package com.example.controledeestoque.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.Funcionario;

import java.util.ArrayList;
import java.util.List;

public class ListaFuncionario extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView lvFuncionarios;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_funcionario);

        dbHelper      = new DatabaseHelper(this);
        lvFuncionarios = findViewById(R.id.lvFuncionarios);
        tvEmpty        = findViewById(R.id.tvEmptyFuncionarios);

        List<Funcionario> funcionarios = dbHelper.getAllFuncionarios();

        if (funcionarios.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            lvFuncionarios.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            lvFuncionarios.setVisibility(View.VISIBLE);

            // extrai só os nomes pra exibir na ListView
            List<String> nomes = new ArrayList<>();
            for (Funcionario f : funcionarios) {
                nomes.add(f.getNome());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    nomes
            );
            lvFuncionarios.setAdapter(adapter);
        }
    }
}