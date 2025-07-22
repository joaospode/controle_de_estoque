package com.example.controledeestoque.util;

import android.content.Context;
import android.util.Log;

import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.Funcionario;
import com.example.controledeestoque.model.Uniforme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Utilitário para gerar e exportar backup em texto.
 */
public class BackupUtils {
    private static final String TAG = "BackupUtils";

    /**
     * Gera o conteúdo do backup como texto.
     */
    public static String generateBackupText(Context context) {
        DatabaseHelper db = new DatabaseHelper(context);
        StringBuilder sb = new StringBuilder();

        // Cabeçalho
        String dataHoje = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                .format(new Date());
        sb.append("IMPORTADO EM: ")
                .append(dataHoje)
                .append("\n");

        // Seção Uniformes
        sb.append("----------UNIFORMES---------\n");
        List<Uniforme> uniformes = db.getAllUniformesComEstoque();
        for (Uniforme u : uniformes) {
            sb.append("Tipo: ").append(u.getTipo()).append("\n");
            sb.append("CA: ").append(u.getCa()).append("\n");
            sb.append("Quantidade: ").append(u.getQuantidadeEstoque()).append("\n");
            sb.append("---------------------------------\n");
        }

        // Seção Funcionários
        sb.append("--------FUNCIONÁRIOS-------\n");
        List<Funcionario> funcionarios = db.getAllFuncionarios();
        for (Funcionario f : funcionarios) {
            sb.append("Nome: ").append(f.getNome()).append("\n");
            sb.append("CPF: ").append(f.getCpf()).append("\n");
            sb.append("Setor: ").append(f.getSetor()).append("\n");
            sb.append("Função: ").append(f.getFuncao()).append("\n");
            sb.append("Data de Admissão: ").append(f.getDataAdmissao()).append("\n");
            sb.append("---------------------------------\n");
        }

        return sb.toString();
    }

    /**
     * Exporta o backup para um arquivo de texto no storage privado do app.
     */
    public static File exportBackup(Context context) throws IOException {
        String content = generateBackupText(context);
        File dir = context.getExternalFilesDir(null);
        if (dir == null) {
            dir = context.getFilesDir();
        }
        String filename = "backup_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                        .format(new Date()) + ".txt";
        File file = new File(dir, filename);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
            fos.flush();
        }

        Log.i(TAG, "Backup salvo em: " + file.getAbsolutePath());
        return file;
    }
}
