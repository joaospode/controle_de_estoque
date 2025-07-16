package com.example.controledeestoque.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private Spinner spFunc, spUni, spQtd;
    private EditText etData;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregar);

        db      = new DatabaseHelper(this);
        spFunc  = findViewById(R.id.spinnerFuncionarios);
        spUni   = findViewById(R.id.spinnerUniformes);
        spQtd   = findViewById(R.id.spinnerQuantidade);
        etData  = findViewById(R.id.etDataEntrega);
        Button btn = findViewById(R.id.btnEntregar);

        // Popula funcionários e uniformes (igual antes)
        List<Funcionario> funcs = db.getAllFuncionarios();
        ArrayAdapter<Funcionario> adapterF = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, funcs
        );
        adapterF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFunc.setAdapter(adapterF);

        List<Uniforme> unis = db.getAllUniformes();
        ArrayAdapter<Uniforme> adapterU = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, unis
        );
        adapterU.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUni.setAdapter(adapterU);

        // Popula spinner de quantidade (1 a 20)
        List<String> qtyList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) qtyList.add(String.valueOf(i));
        ArrayAdapter<String> adapterQ = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, qtyList
        );
        adapterQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQtd.setAdapter(adapterQ);

        // DatePicker
        etData.setOnClickListener(v -> showDatePicker());

        // Registro
        btn.setOnClickListener(v -> {
            try {
                Funcionario f = (Funcionario) spFunc.getSelectedItem();
                Uniforme u    = (Uniforme) spUni.getSelectedItem();
                int qtd       = Integer.parseInt((String) spQtd.getSelectedItem());
                String date   = etData.getText().toString();

                long id = db.insertEntrega(f.getId(), u.getId(), qtd, date);
                if (id > 0) {
                    Toast.makeText(this, "Entrega registrada (ID="+id+")",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao registrar entrega.",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Preencha todos os campos corretamente.",
                        Toast.LENGTH_SHORT).show();
            }
        });
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