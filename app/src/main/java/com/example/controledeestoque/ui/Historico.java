package com.example.controledeestoque.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.HistoricoEntrega;

import java.util.List;

public class Historico extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private RecyclerView rvHistory;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        // Inicializa DB e views
        dbHelper = new DatabaseHelper(this);
        rvHistory = findViewById(R.id.rvHistory);
        tvEmpty = findViewById(R.id.tvEmpty);

        // Busca entregas e popula RecyclerView
        List<HistoricoEntrega> deliveries = dbHelper.getAllDeliveries();
        if (deliveries == null || deliveries.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvHistory.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvHistory.setVisibility(View.VISIBLE);
            rvHistory.setLayoutManager(new LinearLayoutManager(this));
            rvHistory.setAdapter(new HistoricoAdapter(deliveries));
        }
    }
}