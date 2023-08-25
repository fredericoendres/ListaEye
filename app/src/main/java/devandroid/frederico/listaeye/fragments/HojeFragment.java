package devandroid.frederico.listaeye.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import devandroid.frederico.listaeye.R;
import devandroid.frederico.listaeye.controller.PessoaAdapter;
import devandroid.frederico.listaeye.database.ListaEyeDB;
import devandroid.frederico.listaeye.model.Pessoa;


public class HojeFragment extends Fragment {

    View view;
    private RecyclerView recyclerView;
    private PessoaAdapter adapter;
    private ListaEyeDB listaEyeDB;


    public HojeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hoje, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaEyeDB = new ListaEyeDB(getContext());
        List<Pessoa> pessoaList = listaEyeDB.listarDadosHoje();


        adapter = new PessoaAdapter(pessoaList, pessoaId -> {
            CadastroFragment fragment = new CadastroFragment();
            Bundle args = new Bundle();
            args.putInt("id", pessoaId);
            fragment.setArguments(args);

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.content_fragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });


        adapter.setOnDeleteClickListener(pessoa -> {
            Snackbar snackbar = Snackbar.make(recyclerView, "Pessoa a ser deletada", Snackbar.LENGTH_LONG)
                    .setAction("CONFIRMAR", view -> {
                        listaEyeDB.deletarObjeto(pessoa);
                        pessoaList.remove(pessoa);
                        adapter.notifyDataSetChanged();
                    });
            snackbar.show();
        });

        recyclerView.setAdapter(adapter);
        return view;
    }
}
