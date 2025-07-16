package com.example.controledeestoque.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.controledeestoque.R;
import com.example.controledeestoque.data.DatabaseHelper;
import com.example.controledeestoque.model.Uniforme;
import java.util.ArrayList;
import java.util.List;

public class RemoverEstoque extends AppCompatActivity {
    private Spinner spUni, spQtd;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_remover_estoque);
        db  = new DatabaseHelper(this);
        spUni = findViewById(R.id.spinnerUniformes);
        spQtd = findViewById(R.id.spinnerQuantidadeRemove);
        Button btn = findViewById(R.id.btnRemoveStock);

        // popula spinner de uniformes
        List<Uniforme> list = db.getAllUniformes();
        ArrayAdapter<Uniforme> adapterU = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, list
        );
        adapterU.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUni.setAdapter(adapterU);

        // popula quantidade 1..20
        List<String> qty = new ArrayList<>();
        for (int i=1;i<=20;i++) qty.add(String.valueOf(i));
        ArrayAdapter<String> adapterQ = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, qty
        );
        adapterQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQtd.setAdapter(adapterQ);

        btn.setOnClickListener(v -> {
            Uniforme u = (Uniforme) spUni.getSelectedItem();
            int q = Integer.parseInt((String) spQtd.getSelectedItem());
            boolean ok = db.removeStock(u.getId(), q);
            Toast.makeText(
                    this,
                    ok ? "Estoque atualizado." : "Estoque insuficiente.",
                    Toast.LENGTH_SHORT
            ).show();
            if (ok) finish();
        });
    }
}