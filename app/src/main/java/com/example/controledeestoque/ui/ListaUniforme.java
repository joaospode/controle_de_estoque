package com.example.controledeestoque.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.Uniforme;

import java.util.ArrayList;
import java.util.List;

public class ListaUniforme extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView lvUniformes;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_uniforme);

        dbHelper   = new DatabaseHelper(this);
        lvUniformes = findViewById(R.id.lvUniformes);
        tvEmpty     = findViewById(R.id.tvEmptyUniformes);

        List<Uniforme> uniformes = dbHelper.getAllUniformes();

        if (uniformes.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            lvUniformes.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            lvUniformes.setVisibility(View.VISIBLE);

            List<String> tipos = new ArrayList<>();
            for (Uniforme u : uniformes) {
                tipos.add(u.getTipo());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    tipos
            );
            lvUniformes.setAdapter(adapter);
        }
    }
}