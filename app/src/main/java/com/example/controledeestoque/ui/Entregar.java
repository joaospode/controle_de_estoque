package com.example.controledeestoque.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.EntregaItem;
import com.example.controledeestoque.model.Funcionario;
import com.example.controledeestoque.model.Uniforme;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Entregar extends AppCompatActivity {

    private Spinner spFunc;
    private EditText etData;
    private RecyclerView rvUniformes;
    private EntregaUniformeAdapter adapter;
    private DatabaseHelper db;
    private Button btnEntregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregar);

        db = new DatabaseHelper(this);
        spFunc = findViewById(R.id.spinnerFuncionarios);
        etData = findViewById(R.id.etDataEntrega);
        rvUniformes = findViewById(R.id.rvUniformes);
        btnEntregar = findViewById(R.id.btnEntregar);

        carregarFuncionarios();
        carregarUniformes();

        etData.setOnClickListener(v -> showDatePicker());

        btnEntregar.setOnClickListener(v -> registrarEntregas());
    }

    private void carregarFuncionarios() {
        List<Funcionario> funcs = db.getAllFuncionarios();
        ArrayAdapter<Funcionario> adapterF = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, funcs
        );
        adapterF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFunc.setAdapter(adapterF);
    }

    private void carregarUniformes() {
        List<Uniforme> unis = db.getAllUniformes();
        List<EntregaItem> entregaItens = new ArrayList<>();

        for (Uniforme u : unis) {
            entregaItens.add(new EntregaItem(u.getId(), u.getTipo()));
        }

        adapter = new EntregaUniformeAdapter(entregaItens);
        rvUniformes.setLayoutManager(new LinearLayoutManager(this));
        rvUniformes.setAdapter(adapter);
    }

    private void registrarEntregas() {
        try {
            Funcionario f = (Funcionario) spFunc.getSelectedItem();
            String data = etData.getText().toString();

            if (f == null || data.isEmpty()) {
                Toast.makeText(this, "Preencha funcionário e data.", Toast.LENGTH_SHORT).show();
                return;
            }

            List<EntregaItem> itens = adapter.getItens();
            int entregasFeitas = 0;

            for (EntregaItem item : itens) {
                int qtd = item.getQuantidade();
                if (qtd > 0) {
                    long id = db.insertEntrega(f.getId(), item.getUniformeId(), qtd, data);
                    if (id > 0) entregasFeitas++;
                }
            }

            if (entregasFeitas > 0) {
                Toast.makeText(this, "Entrega registrada com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Nenhum uniforme foi selecionado para entrega.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Erro ao registrar entrega.", Toast.LENGTH_LONG).show();
        }
    }

    private void showDatePicker() {
        final Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, day) -> {
                    cal.set(year, month, day);
                    String str = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                            .format(cal.getTime());
                    etData.setText(str);
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}