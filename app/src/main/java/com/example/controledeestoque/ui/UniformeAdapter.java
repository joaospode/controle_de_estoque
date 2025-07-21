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
    private final List<Uniforme> data;

    public UniformeAdapter(List<Uniforme> data) {
        this.data = data;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_uniforme, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        h.tvTipo.setText(data.get(pos).getTipo());
    }

    @Override public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipo;
        ViewHolder(@NonNull View item) {
            super(item);
            tvTipo = item.findViewById(R.id.tvTipoUniforme);
        }
    }
}
