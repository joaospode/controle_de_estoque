package com.example.controledeestoque.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controledeestoque.R;
import com.example.controledeestoque.model.Uniforme;

import java.util.List;

public class UniformeAdapter extends RecyclerView.Adapter<UniformeAdapter.ViewHolder> {

    private List<Uniforme> uniformes;

    public UniformeAdapter(List<Uniforme> uniformes) {
        this.uniformes = uniformes;
    }

    public void updateList(List<Uniforme> novos) {
        this.uniformes = novos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UniformeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_uniforme, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniformeAdapter.ViewHolder holder, int position) {
        Uniforme u = uniformes.get(position);
        holder.tvTipo.setText(u.getTipo());
        holder.tvCA.setText("CA: " + u.getCa());
        holder.tvQuantidade.setText("Quantidade: " + u.getQuantidadeEstoque());
    }

    @Override
    public int getItemCount() {
        return uniformes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipo, tvCA, tvQuantidade;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvCA = itemView.findViewById(R.id.tvCA);
            tvQuantidade = itemView.findViewById(R.id.tvQuantidade);
        }
    }
}