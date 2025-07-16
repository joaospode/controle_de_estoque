package com.example.controledeestoque.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.Funcionario;

public class AdicionarFuncionario extends AppCompatActivity {
    private EditText etNome, etDataNascimento, etCpf, etSetor, etFuncao, etDataAdmissao;
    private Button btnSalvar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_funcionario);

        // Inicializa helper e encontra as views
        dbHelper = new DatabaseHelper(this);
        etNome           = findViewById(R.id.etNome);
        etDataNascimento = findViewById(R.id.etDataNascimento);
        etCpf            = findViewById(R.id.etCpf);
        etSetor          = findViewById(R.id.etSetor);
        etFuncao         = findViewById(R.id.etFuncao);
        etDataAdmissao   = findViewById(R.id.etDataAdmissao);
        btnSalvar        = findViewById(R.id.btnSalvarFuncionario);

        btnSalvar.setOnClickListener(v -> salvarFuncionario());
    }

    private void salvarFuncionario() {
        // 1) Lê e valida campos
        String nome  = etNome.getText().toString().trim();
        String nasc  = etDataNascimento.getText().toString().trim();
        String cpf   = etCpf.getText().toString().trim();
        String setor = etSetor.getText().toString().trim();
        String func  = etFuncao.getText().toString().trim();
        String adm   = etDataAdmissao.getText().toString().trim();

        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(cpf)) {
            Toast.makeText(this, "Nome e CPF são obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2) Cria objeto Funcionario
        Funcionario f = new Funcionario();
        f.setNome(nome);
        f.setDataNascimento(nasc);
        f.setCpf(cpf);
        f.setSetor(setor);
        f.setFuncao(func);
        f.setDataAdmissao(adm);

        // 3) Insere no banco
        long id = dbHelper.insertFuncionario(f);
        // (insertFuncionario(Funcionario f) definido em DatabaseHelper)

        // 4) Feedback e finaliza
        if (id > 0) {
            Toast.makeText(this, "Funcionário salvo (ID=" + id + ").", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar funcionário.", Toast.LENGTH_LONG).show();
        }
    }
}