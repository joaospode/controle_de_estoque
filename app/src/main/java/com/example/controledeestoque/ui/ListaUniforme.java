package com.example.controledeestoque.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.Uniforme;

import java.util.List;

public class ListaUniforme extends AppCompatActivity {

    private RecyclerView rvUniformes;
    private TextView tvEmpty;
    private DatabaseHelper dbHelper;
    private UniformeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_uniforme);

        rvUniformes = findViewById(R.id.rvUniformes);
        tvEmpty = findViewById(R.id.tvEmptyUniformes);
        dbHelper = new DatabaseHelper(this);

        rvUniformes.setLayoutManager(new LinearLayoutManager(this));

        List<Uniforme> uniformes = dbHelper.getAllUniformesComEstoque();
        adapter = new UniformeAdapter(uniformes);
        rvUniformes.setAdapter(adapter);

        if (uniformes.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvUniformes.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvUniformes.setVisibility(View.VISIBLE);
        }
    }
}