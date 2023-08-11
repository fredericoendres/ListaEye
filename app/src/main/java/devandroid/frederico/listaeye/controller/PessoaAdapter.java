package devandroid.frederico.listaeye.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import devandroid.frederico.listaeye.R;
import devandroid.frederico.listaeye.model.Pessoa;

public class PessoaAdapter extends RecyclerView.Adapter<PessoaAdapter.PessoaViewHolder> {

    private List<Pessoa> pessoaList;
    private OnEditClickListener onEditClickListener;

    public PessoaAdapter(List<Pessoa> pessoaList) {
        if (pessoaList == null) {
            this.pessoaList = new ArrayList<>();
        } else {
            this.pessoaList = pessoaList;
        }
    }

    public PessoaAdapter(List<Pessoa> pessoaList, OnEditClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
        if (pessoaList == null) {
            this.pessoaList = new ArrayList<>();
        } else {
            this.pessoaList = pessoaList;
        }
    }

    public interface OnEditClickListener {
        void onEditClick(int pessoaId);
    }

    public void setOnEditClickListener(OnEditClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }

    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Pessoa pessoa);
    }

    public void updateData(List<Pessoa> newPessoaList) {
        if (newPessoaList == null) {
            return;
        }
        pessoaList = newPessoaList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PessoaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pessoas, parent, false);
        return new PessoaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PessoaViewHolder holder, int position) {
        Pessoa pessoa = pessoaList.get(position);
        String nomeCompleto = pessoa.getPrimeiroNome() + " " + pessoa.getSobrenome();
        holder.textViewNomeCompleto.setText(nomeCompleto);
        holder.textViewTelefone.setText(pessoa.getTelefone());
        holder.textViewGenero.setText((pessoa.getGenero()));
        holder.textViewCpf.setText(pessoa.getCpf());

        holder.btnEditarPessoa.setOnClickListener(view -> {
            if (onEditClickListener != null) {
                onEditClickListener.onEditClick(pessoa.getId());
            }
        });
        holder.btnDeletarPessoa.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(pessoa);
            }
        });


    }

    @Override
    public int getItemCount() {
        return pessoaList.size();
    }

    public static class PessoaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNomeCompleto, textViewTelefone, textViewGenero, textViewSobrenome, textViewCpf;

        ImageButton btnDeletarPessoa;
        ImageButton btnEditarPessoa;


        public PessoaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeCompleto = itemView.findViewById(R.id.textViewNomeCompleto);
            textViewTelefone = itemView.findViewById(R.id.textViewTelefone);
            textViewGenero = itemView.findViewById(R.id.textViewGenero);
            textViewSobrenome = itemView.findViewById(R.id.textViewSobrenome);
            textViewCpf = itemView.findViewById(R.id.textViewCpf);
            btnDeletarPessoa = itemView.findViewById(R.id.btnDeletarPessoa);
            btnEditarPessoa = itemView.findViewById(R.id.btnEditarPessoa);
        }
    }


}
