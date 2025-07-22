package com.example.controledeestoque.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.Funcionario;

import java.util.List;

public class ListaFuncionario extends AppCompatActivity {
    private RecyclerView rvFuncionarios;
    private TextView tvEmpty;
    private DatabaseHelper dbHelper;
    private FuncionarioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_funcionario);

        rvFuncionarios = findViewById(R.id.rvFuncionarios);
        tvEmpty = findViewById(R.id.tvEmptyFuncionarios);
        dbHelper = new DatabaseHelper(this);

        rvFuncionarios.setLayoutManager(new LinearLayoutManager(this));

        // Carrega todos os funcionários com detalhes
        List<Funcionario> funcionarios = dbHelper.getAllFuncionariosDetalhado();
        adapter = new FuncionarioAdapter(funcionarios);
        rvFuncionarios.setAdapter(adapter);

        // Exibe mensagem se a lista estiver vazia
        if (funcionarios.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvFuncionarios.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvFuncionarios.setVisibility(View.VISIBLE);
        }
    }
}
