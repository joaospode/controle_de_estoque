package com.example.controledeestoque.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controledeestoque.R;
import com.example.controledeestoque.model.Funcionario;

import java.util.List;

public class FuncionarioAdapter extends RecyclerView.Adapter<FuncionarioAdapter.ViewHolder> {
    private final List<Funcionario> data;

    public FuncionarioAdapter(List<Funcionario> data) {
        this.data = data;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_funcionario, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        h.tvNome.setText(data.get(pos).getNome());
    }

    @Override public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome;
        ViewHolder(@NonNull View item) {
            super(item);
            tvNome = item.findViewById(R.id.tvNomeFuncionario);
        }
    }
}
