package com.example.controledeestoque.ui;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.Funcionario;
import com.example.controledeestoque.model.Uniforme;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Entregar extends AppCompatActivity {

    private Spinner spFuncionarios;
    private EditText etDataEntrega;
    private LinearLayout layoutItens;
    private Button btnAddUniforme, btnEntregar;
    private DatabaseHelper db;
    private List<Uniforme> listaUniformes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregar);

        db = new DatabaseHelper(this);
        spFuncionarios = findViewById(R.id.spinnerFuncionarios);
        etDataEntrega = findViewById(R.id.etDataEntrega);
        layoutItens = findViewById(R.id.containerUniformes);
        btnAddUniforme = findViewById(R.id.btnAddUniforme);
        btnEntregar = findViewById(R.id.btnEntregar);

        preencherFuncionarios();
        listaUniformes = db.getAllUniformes();
        adicionarItemUniforme();

        etDataEntrega.setOnClickListener(v -> mostrarDatePicker());
        btnAddUniforme.setOnClickListener(v -> adicionarItemUniforme());
        btnEntregar.setOnClickListener(v -> iniciarEntregaEmBackground());
    }

    private void preencherFuncionarios() {
        List<Funcionario> funcionarios = db.getAllFuncionarios();
        ArrayAdapter<Funcionario> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                funcionarios
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFuncionarios.setAdapter(adapter);
    }

    private void adicionarItemUniforme() {
        View itemView = LayoutInflater.from(this)
                .inflate(R.layout.row_uniforme, layoutItens, false);
        Spinner spUni = itemView.findViewById(R.id.spinnerUniforme);
        Spinner spQtd = itemView.findViewById(R.id.spinnerQuantidade);

        ArrayAdapter<Uniforme> adapterUni = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaUniformes
        );
        adapterUni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUni.setAdapter(adapterUni);

        List<String> quantidades = new ArrayList<>();
        for (int i = 1; i <= 20; i++) quantidades.add(String.valueOf(i));
        ArrayAdapter<String> adapterQtd = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                quantidades
        );
        adapterQtd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQtd.setAdapter(adapterQtd);

        layoutItens.addView(itemView);
    }

    /**
     * Dispara a operação de entrega em background para evitar ANR.
     */
    private void iniciarEntregaEmBackground() {
        Funcionario funcionario = (Funcionario) spFuncionarios.getSelectedItem();
        String data = etDataEntrega.getText().toString().trim();
        if (funcionario == null || data.isEmpty()) {
            Toast.makeText(this, "Selecione funcionário e data.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Prepara lista de itens
        List<EntregaItem> itens = new ArrayList<>();
        for (int i = 0; i < layoutItens.getChildCount(); i++) {
            View row = layoutItens.getChildAt(i);
            Spinner spUni = row.findViewById(R.id.spinnerUniforme);
            Spinner spQtd = row.findViewById(R.id.spinnerQuantidade);
            Uniforme u = (Uniforme) spUni.getSelectedItem();
            int q = Integer.parseInt((String) spQtd.getSelectedItem());
            if (q > 0) {
                itens.add(new EntregaItem(u.getId(), q));
            }
        }
        if (itens.isEmpty()) {
            Toast.makeText(this, "Nenhum uniforme selecionado.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Executa em thread separada
        new Thread(() -> {
            final int[] sucesso = new int[1];
            SQLiteDatabase sqlDb = db.getWritableDatabase();
            sqlDb.beginTransaction();
            try {
                for (EntregaItem item : itens) {
                    long id = db.insertEntrega(
                            funcionario.getId(),
                            item.getUniformeId(),
                            item.getQuantidade(),
                            data
                    );
                    if (id > 0) sucesso[0]++;
                }
                sqlDb.setTransactionSuccessful();
            } finally {
                sqlDb.endTransaction();
            }
            runOnUiThread(() -> {
                if (sucesso[0] > 0) {
                    Toast.makeText(this, "Entregas registradas: " + sucesso[0], Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Falha ao registrar entregas.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void mostrarDatePicker() {
        final Calendar cal = Calendar.getInstance();
        new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    cal.set(year, month, day);
                    String data = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(cal.getTime());
                    etDataEntrega.setText(data);
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    // Classe auxiliar para itens de entrega
    public static class EntregaItem {
        private final int uniformeId;
        private final int quantidade;
        EntregaItem(int uniformeId, int quantidade) {
            this.uniformeId = uniformeId;
            this.quantidade = quantidade;
        }
        public int getUniformeId() { return uniformeId; }
        public int getQuantidade() { return quantidade; }
    }
}
