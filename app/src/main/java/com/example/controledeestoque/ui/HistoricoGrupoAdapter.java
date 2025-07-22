package com.example.controledeestoque.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controledeestoque.R;
import com.example.controledeestoque.model.EntregaGrupo;
import com.example.controledeestoque.model.HistoricoEntrega;

import java.util.List;

/**
 * Adapter para exibir grupos de entregas, cada grupo contendo múltiplos itens de uniformes.
 */
public class HistoricoGrupoAdapter extends RecyclerView.Adapter<HistoricoGrupoAdapter.ViewHolder> {

    private List<EntregaGrupo> grupos;

    public HistoricoGrupoAdapter(List<EntregaGrupo> grupos) {
        this.grupos = grupos;
    }

    /**
     * Atualiza a lista de grupos e notifica mudanças.
     */
    public void updateList(List<EntregaGrupo> novosGrupos) {
        this.grupos = novosGrupos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historico_grupo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EntregaGrupo grupo = grupos.get(position);
        // Cabeçalho: Funcionário — Data
        holder.tvHeader.setText(grupo.getFuncionario() + " — " + grupo.getDataEntrega());

        // Remove views antigas e infla novos itens
        holder.container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(holder.container.getContext());
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
        LinearLayout container;

        ViewHolder(View itemView) {
            super(itemView);
            tvHeader  = itemView.findViewById(R.id.tvGroupHeader);
            container = itemView.findViewById(R.id.containerUniformes);
        }
    }
}