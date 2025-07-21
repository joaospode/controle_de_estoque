package com.example.controledeestoque.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.HistoricoEntrega;
import com.example.controledeestoque.ui.HistoricoAdapter;

import java.util.List;

public class Historico extends AppCompatActivity {
    private DatabaseHelper      dbHelper;
    private EditText            etFilterFuncionario;
    private EditText            etFilterUniforme;
    private Button              btnFilter;
    private RecyclerView        rvHistory;
    private TextView            tvEmpty;
    private HistoricoAdapter    adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        // 1) Instancia o helper e as views
        dbHelper             = new DatabaseHelper(this);
        etFilterFuncionario  = findViewById(R.id.etFilterFuncionario);
        etFilterUniforme     = findViewById(R.id.etFilterUniforme);
        btnFilter            = findViewById(R.id.btnFilter);
        rvHistory            = findViewById(R.id.rvHistory);
        tvEmpty              = findViewById(R.id.tvEmpty);

        // 2) Configura RecyclerView e Adapter
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        List<HistoricoEntrega> all = dbHelper.getAllDeliveries();
        adapter = new HistoricoAdapter(all);
        rvHistory.setAdapter(adapter);

        // 3) Exibe ou oculta mensagem "vazio"
        toggleEmptyState(all.isEmpty());

        // 4) Configura o botão de filtro
        btnFilter.setOnClickListener(v -> {
            String funcFilter = etFilterFuncionario.getText().toString().trim();
            String uniFilter  = etFilterUniforme.getText().toString().trim();

            // busca filtrada (você deve ter implementado este método no DatabaseHelper)
            List<HistoricoEntrega> filtered = dbHelper.getDeliveriesByFilter(funcFilter, uniFilter);

            // atualiza lista e UI
            adapter.updateList(filtered);
            toggleEmptyState(filtered == null || filtered.isEmpty());
        });
    }

    /**
     * Exibe ou esconde o TextView de "nenhum registro"
     */
    private void toggleEmptyState(boolean isEmpty) {
        if (isEmpty) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvHistory.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvHistory.setVisibility(View.VISIBLE);
        }
    }
}