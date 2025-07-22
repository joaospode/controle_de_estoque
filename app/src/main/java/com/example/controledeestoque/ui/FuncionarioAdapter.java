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

    private List<Funcionario> funcionarios;

    public FuncionarioAdapter(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public void updateList(List<Funcionario> novos) {
        this.funcionarios = novos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FuncionarioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_funcionario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FuncionarioAdapter.ViewHolder holder, int position) {
        Funcionario f = funcionarios.get(position);

        holder.tvNome.setText(f.getNome());
        holder.tvFuncao.setText("Função: "    + f.getFuncao());
        holder.tvSetor.setText("Setor: "      + f.getSetor());
        holder.tvData.setText("Admissão: "    + f.getDataAdmissao());
    }

    @Override
    public int getItemCount() {
        return funcionarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvFuncao, tvSetor, tvData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvFuncao = itemView.findViewById(R.id.tvFuncao);
            tvSetor = itemView.findViewById(R.id.tvSetor);
            tvData = itemView.findViewById(R.id.tvData);
        }
    }
}