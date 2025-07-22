package com.example.controledeestoque.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;

import com.example.controledeestoque.model.HistoricoEntrega;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Utilitário para gerar e imprimir o Termo de Entrega em PDF.
 */
public class TermoGenerator {

    /**
     * Gera um PDF com os dados do termo e retorna o arquivo.
     * Agora aceita nome do funcionário e lista de HistoricoEntrega diretamente.
     */
    public static File generateTermoPdf(
            Context context,
            String nomeFuncionario,
            List<HistoricoEntrega> itens,
            String dataEntrega
    ) throws IOException {
        PdfDocument pdf = new PdfDocument();
        PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdf.startPage(info);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setTextSize(12);
        int x = 30, y = 30;

        paint.setFakeBoldText(true);
        canvas.drawText("TERMO DE ENTREGA DO UNIFORME", x, y, paint);
        paint.setFakeBoldText(false);
        y += 30;

        String numero = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        canvas.drawText("NÚMERO: " + numero, x, y, paint);
        y += 20;

        canvas.drawText("Funcionário: " + nomeFuncionario, x, y, paint);
        y += 30;

        canvas.drawText("UNIFORME          QTDE          ENTREGA", x, y, paint);
        y += 20;
        for (HistoricoEntrega h : itens) {
            canvas.drawText(h.getUniforme(), x, y, paint);
            canvas.drawText(String.valueOf(h.getQuantidade()), x + 200, y, paint);
            canvas.drawText(dataEntrega, x + 300, y, paint);
            y += 20;
            if (y > 800) break;
        }

        y += 40;
        String ano = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        canvas.drawText("Içara, ___ de _______ de " + ano, x, y, paint);
        y += 20;
        canvas.drawText("Assinatura: ____________________________", x, y, paint);

        pdf.finishPage(page);

        File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (dir != null && !dir.exists()) dir.mkdirs();
        File file = new File(dir, "termo_" + numero + ".pdf");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            pdf.writeTo(fos);
        }
        pdf.close();
        return file;
    }

    /**
     * Imprime o PDF gerado usando o PrintManager.
     */
    public static void printPdf(Context context, File pdfFile) {
        PrintManager manager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter adapter = new PdfDocumentAdapter(context, pdfFile.getAbsolutePath());
        manager.print("TermoEntrega", adapter, new PrintAttributes.Builder().build());
    }
}