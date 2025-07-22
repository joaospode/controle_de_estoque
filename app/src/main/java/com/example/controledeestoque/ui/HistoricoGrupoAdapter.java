package com.example.controledeestoque.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controledeestoque.R;
import com.example.controledeestoque.model.EntregaGrupo;
import com.example.controledeestoque.model.HistoricoEntrega;
import com.example.controledeestoque.util.TermoGenerator;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HistoricoGrupoAdapter extends RecyclerView.Adapter<HistoricoGrupoAdapter.ViewHolder> {

    private List<EntregaGrupo> grupos;
    private Context context;

    public HistoricoGrupoAdapter(Context context, List<EntregaGrupo> grupos) {
        this.context = context;
        this.grupos = grupos;
    }

    public void updateList(List<EntregaGrupo> novosGrupos) {
        this.grupos = novosGrupos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_historico_grupo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EntregaGrupo grupo = grupos.get(position);
        holder.tvHeader.setText(grupo.getFuncionario() + " — " + grupo.getDataEntrega());

        // ASSINAR gera termo e imprime
        holder.btnAssinar.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    File pdf = TermoGenerator.generateTermoPdf(
                            context,
                            // converte DeliveryGroup para Funcionario e items
                            grupo.getFuncionario(),
                            grupo.getItens(),
                            grupo.getDataEntrega()
                    );
                    ((Activity) context).runOnUiThread(() -> TermoGenerator.printPdf(context, pdf));
                } catch (IOException e) {
                    ((Activity) context).runOnUiThread(() ->
                            Toast.makeText(context, "Erro ao gerar termo: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }
            }).start();
        });

        holder.container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(context);
        for (HistoricoEntrega item : grupo.getItens()) {
            View row = inflater.inflate(
                    R.layout.item_historico_uniforme,
                    holder.container,
                    false
            );
            TextView tvTipo = row.findViewById(R.id.tvHistoUniformeTipo);
            TextView tvQtd  = row.findViewById(R.id.tvHistoUniformeQtd);
            tvTipo.setText(item.getUniforme());
            tvQtd.setText(String.valueOf(item.getQuantidade()));
            holder.container.addView(row);
        }
    }

    @Override
    public int getItemCount() {
        return grupos == null ? 0 : grupos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeader;
        Button btnAssinar;
        LinearLayout container;

        ViewHolder(View itemView) {
            super(itemView);
            tvHeader   = itemView.findViewById(R.id.tvGroupHeader);
            btnAssinar = itemView.findViewById(R.id.btnApi);
            container  = itemView.findViewById(R.id.containerUniformes);
        }
    }
}