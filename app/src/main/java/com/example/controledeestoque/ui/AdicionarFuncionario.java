package com.example.controledeestoque.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

        spSetor.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String setorSelecionado = (String) parent.getItemAtPosition(position);
                carregarFuncoesPorSetor(setorSelecionado);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Nenhuma ação
            }
        });

        btnSalvar.setOnClickListener(v -> salvarFuncionario());
    }

    private void carregarSetores() {
        List<String> setores = dbHelper.getAllSetores();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, setores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSetor.setAdapter(adapter);
    }

    private void carregarFuncoesPorSetor(String setor) {
        List<String> funcoes = dbHelper.getFuncoesPorSetor(setor);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, funcoes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFuncao.setAdapter(adapter);
    }

    private void showDatePicker(EditText targetField) {
        final Calendar cal = Calendar.getInstance();
        DatePickerDialog picker = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    cal.set(year, month, dayOfMonth);
                    String formatted = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.getTime());
                    targetField.setText(formatted);
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }

    private void salvarFuncionario() {
        String nome = etNome.getText().toString().trim();
        String cpf = etCpf.getText().toString().trim();
        String nascimento = etDataNascimento.getText().toString().trim();
        String admissao = etDataAdmissao.getText().toString().trim();
        String setor = (String) spSetor.getSelectedItem();
        String funcao = (String) spFuncao.getSelectedItem();

        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(cpf)) {
            Toast.makeText(this, "Nome e CPF são obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        Funcionario f = new Funcionario();
        f.setNome(nome);
        f.setCpf(cpf);
        f.setDataNascimento(nascimento);
        f.setDataAdmissao(admissao);
        f.setSetor(setor);
        f.setFuncao(funcao);

        long id = dbHelper.insertFuncionario(f);
        if (id > 0) {
            Toast.makeText(this, "Funcionário salvo (ID=" + id + ").", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar funcionário.", Toast.LENGTH_LONG).show();
        }
    }
}