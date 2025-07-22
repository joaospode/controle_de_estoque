package com.example.controledeestoque.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.Funcionario;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdicionarFuncionario extends AppCompatActivity {
    private EditText etNome, etCpf, etDataNascimento, etDataAdmissao;
    private Spinner spSetor, spFuncao;
    private Button btnSalvar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_funcionario);

        dbHelper = new DatabaseHelper(this);

        etNome = findViewById(R.id.etNome);
        etCpf = findViewById(R.id.etCpf);
        etDataNascimento = findViewById(R.id.etDataNascimento);
        etDataAdmissao = findViewById(R.id.etDataAdmissao);
        spSetor = findViewById(R.id.spSetor);
        spFuncao = findViewById(R.id.spFuncao);
        btnSalvar = findViewById(R.id.btnSalvarFuncionario);

        etDataNascimento.setOnClickListener(v -> showDatePicker(etDataNascimento));
        etDataAdmissao.setOnClickListener(v -> showDatePicker(etDataAdmissao));

        carregarSetores();

        spSetor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String setor = (String) parent.getItemAtPosition(position);
                carregarFuncoesPorSetor(setor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nenhuma ação
            }
        });

        btnSalvar.setOnClickListener(v -> salvarFuncionario());
    }

    private void carregarSetores() {
        List<String> setores = dbHelper.getAllSetores();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                setores
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSetor.setAdapter(adapter);
    }

    private void carregarFuncoesPorSetor(String setor) {
        List<String> funcoes = dbHelper.getFuncoesBySetor(setor);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                funcoes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFuncao.setAdapter(adapter);
    }

    private void showDatePicker(EditText targetField) {
        final Calendar cal = Calendar.getInstance();
        DatePickerDialog picker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    cal.set(year, month, dayOfMonth);
                    String formatted = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                            .format(cal.getTime());
                    targetField.setText(formatted);
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        picker.show();
    }

    private void salvarFuncionario() {
        String nome = etNome.getText().toString().trim();
        String cpf = etCpf.getText().toString().trim();
        String nasc = etDataNascimento.getText().toString().trim();
        String adm = etDataAdmissao.getText().toString().trim();
        String setor = (String) spSetor.getSelectedItem();
        String funcao = (String) spFuncao.getSelectedItem();

        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(cpf) || setor == null || funcao == null) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        Funcionario f = new Funcionario();
        f.setNome(nome);
        f.setCpf(cpf);
        f.setDataNascimento(nasc);
        f.setDataAdmissao(adm);
        f.setSetor(setor);
        f.setFuncao(funcao);

        long id = dbHelper.insertFuncionario(f);
        if (id > 0) {
            Toast.makeText(this, "Funcionário salvo (ID=" + id + ")", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar funcionário.", Toast.LENGTH_LONG).show();
        }
    }
}