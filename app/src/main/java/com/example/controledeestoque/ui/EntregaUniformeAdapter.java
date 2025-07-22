package com.example.controledeestoque.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controledeestoque.R;
import com.example.controledeestoque.model.EntregaItem;

import java.util.List;

public class EntregaUniformeAdapter extends RecyclerView.Adapter<EntregaUniformeAdapter.ViewHolder> {

    private List<EntregaItem> lista;

    public EntregaUniformeAdapter(List<EntregaItem> lista) {
        this.lista = lista;
    }

    public List<EntregaItem> getItens() {
        return lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entrega_uniforme, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EntregaItem item = lista.get(position);
        holder.tvTipo.setText(item.getTipo());

        holder.etQtd.setText(item.getQuantidade() > 0 ? String.valueOf(item.getQuantidade()) : "");
        holder.etQtd.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                try {
                    item.setQuantidade(Integer.parseInt(s.toString()));
                } catch (NumberFormatException e) {
                    item.setQuantidade(0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipo;
        EditText etQtd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipo = itemView.findViewById(R.id.tvTipoUniforme);
            etQtd = itemView.findViewById(R.id.etQtdEntrega);
        }
    }
}