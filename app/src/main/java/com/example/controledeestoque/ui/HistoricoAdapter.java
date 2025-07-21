package com.example.controledeestoque.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controledeestoque.R;
import com.example.controledeestoque.model.HistoricoEntrega;

import java.util.ArrayList;
import java.util.List;

public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.HistoryViewHolder> {
    private final List<HistoricoEntrega> data;

    public HistoricoAdapter(List<HistoricoEntrega> initialData) {
        // evita NullPointerException
        this.data = (initialData != null) ? new ArrayList<>(initialData) : new ArrayList<>();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historico, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoricoEntrega item = data.get(position);
        holder.tvFuncionario.setText("Funcionário: " + item.getFuncionario());
        holder.tvUniforme.setText("Uniforme: "   + item.getUniforme());
        holder.tvQuantidade.setText("Quantidade: " + item.getQuantidade());
        holder.tvData.setText("Data: "            + item.getDataEntrega());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * Atualiza a lista interna do adapter e força redraw.
     */
    public void updateList(List<HistoricoEntrega> newList) {
        data.clear();
        if (newList != null) {
            data.addAll(newList);
        }
        notifyDataSetChanged();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        final TextView tvFuncionario, tvUniforme, tvQuantidade, tvData;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFuncionario = itemView.findViewById(R.id.tvFuncionario);
            tvUniforme   = itemView.findViewById(R.id.tvUniforme);
            tvQuantidade = itemView.findViewById(R.id.tvQuantidade);
            tvData       = itemView.findViewById(R.id.tvData);
        }
    }
}