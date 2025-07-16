package com.example.controledeestoque.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.controledeestoque.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Configuracoes extends AppCompatActivity {
    private Button btnBackup;
    private ActivityResultLauncher<String> storagePermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        btnBackup = findViewById(R.id.btnBackup);
        storagePermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) doBackup();
                    else Toast.makeText(this, "Permissão negada.", Toast.LENGTH_SHORT).show();
                }
        );

        btnBackup.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    doBackup();
                }
            } else {
                doBackup();
            }
        });
    }

    private void doBackup() {
        // Origem: arquivo do DB interno
        File dbFile = getDatabasePath("uniformes.db");
        // Destino: pasta Downloads do usuário
        File downloads = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
        );
        File outFile = new File(downloads, "uniformes_backup.db");

        try (FileInputStream fis = new FileInputStream(dbFile);
             FileOutputStream fos = new FileOutputStream(outFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            fos.flush();
            Toast.makeText(this,
                    "Backup salvo em: " + outFile.getAbsolutePath(),
                    Toast.LENGTH_LONG
            ).show();
        } catch (IOException e) {
            Toast.makeText(this,
                    "Erro ao fazer backup: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}