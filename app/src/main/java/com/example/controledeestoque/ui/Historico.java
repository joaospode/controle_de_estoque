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
import com.example.controledeestoque.model.EntregaGrupo;
import com.example.controledeestoque.model.HistoricoEntrega;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Historico extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText etFilterFuncionario;
    private EditText etFilterUniforme;
    private Button btnFilter;
    private RecyclerView rvHistory;
    private TextView tvEmpty;
    private HistoricoGrupoAdapter adapter;

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

        // 2) Configura RecyclerView
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        // 3) Carrega e exibe dados sem filtro
        loadAndDisplay(null, null);

        // 4) Configura o botão de filtro
        btnFilter.setOnClickListener(v -> {
            String funcFilter = etFilterFuncionario.getText().toString().trim();
            String uniFilter  = etFilterUniforme.getText().toString().trim();
            loadAndDisplay(funcFilter, uniFilter);
        });
    }

    /**
     * Carrega entregas (com ou sem filtros), agrupa por funcionário+data e atualiza o adapter.
     */
    private void loadAndDisplay(String funcFilter, String uniFilter) {
        List<HistoricoEntrega> all;
        if ((funcFilter == null || funcFilter.isEmpty()) && (uniFilter == null || uniFilter.isEmpty())) {
            all = dbHelper.getAllDeliveries();
        } else {
            all = dbHelper.getDeliveriesByFilter(funcFilter, uniFilter);
        }

        // Agrupa por funcionário e data
        Map<String, EntregaGrupo> map = new LinkedHashMap<>();
        for (HistoricoEntrega h : all) {
            String key = h.getFuncionario() + "_" + h.getDataEntrega();
            if (!map.containsKey(key)) {
                map.put(key, new EntregaGrupo(
                        h.getFuncionario(),
                        h.getDataEntrega(),
                        new ArrayList<>()
                ));
            }
            map.get(key).getItens().add(h);
        }
        List<EntregaGrupo> groups = new ArrayList<>(map.values());

        // Configura ou atualiza o adapter
        if (adapter == null) {
            adapter = new HistoricoGrupoAdapter(this, groups);
            rvHistory.setAdapter(adapter);
        } else {
            adapter.updateList(groups);
        }

        // Exibe ou oculta mensagem "vazio"
        toggleEmptyState(groups.isEmpty());
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