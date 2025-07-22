package com.example.controledeestoque.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controledeestoque.R;
import com.example.controledeestoque.util.BackupUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Configuracoes extends AppCompatActivity {
    private Button btnBackup;
    private Button btnListaFuncionarios;
    private Button btnListaUniformes;

    // Launcher para criar documento no sistema (Downloads ou local escolhido)
    private ActivityResultLauncher<Intent> createDocumentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        btnBackup = findViewById(R.id.btnBackup);
        btnListaFuncionarios = findViewById(R.id.btnListaFuncionarios);
        btnListaUniformes = findViewById(R.id.btnListaUniformes);

        // Inicializa launcher para ACTION_CREATE_DOCUMENT
        createDocumentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            saveBackupToUri(uri);
                        }
                    }
                }
        );

        // Botão de backup dispara diálogo de salvar arquivo
        btnBackup.setOnClickListener(v -> {
            String fileName = "backup_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                    .format(new Date()) + ".txt";
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TITLE, fileName);
            createDocumentLauncher.launch(intent);
        });

        btnListaFuncionarios.setOnClickListener(v ->
                startActivity(new Intent(this, ListaFuncionario.class))
        );

        btnListaUniformes.setOnClickListener(v ->
                startActivity(new Intent(this, ListaUniforme.class))
        );
    }

    private void saveBackupToUri(Uri uri) {
        try (OutputStream os = getContentResolver().openOutputStream(uri)) {
            if (os == null) throw new IOException("Cannot open output stream");
            String content = BackupUtils.generateBackupText(this);
            os.write(content.getBytes(StandardCharsets.UTF_8));
            os.flush();
            Toast.makeText(this,
                    "Backup salvo em: " + uri.toString(),
                    Toast.LENGTH_LONG
            ).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,
                    "Erro ao salvar backup: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}