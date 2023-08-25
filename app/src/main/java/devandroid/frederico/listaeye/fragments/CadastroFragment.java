package devandroid.frederico.listaeye.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import devandroid.frederico.listaeye.MainActivity;
import devandroid.frederico.listaeye.R;
import devandroid.frederico.listaeye.controller.PessoaController;
import devandroid.frederico.listaeye.model.Pessoa;


public class CadastroFragment extends Fragment {

    View view;
    Button btnLimpar;
    Button btnSalvar;
    ImageButton btnVoltar;
    List<Pessoa> dados;
    Spinner spinner;
    PessoaController controller;
    Pessoa pessoa;
    List<String> tiposDeGenero;
    EditText editPrimeiroNome;
    EditText editSobrenome;
    EditText editCpf;
    EditText editTelefone;
    private String selectedGenero;

    public CadastroFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cadastro, container, false);
        Log.d("1", "1 teste");
        controller = new PessoaController(getActivity());
        dados = controller.getListaDeDados();
        tiposDeGenero = controller.dadosParaSpinner();

        pessoa = new Pessoa();

        editPrimeiroNome = view.findViewById(R.id.editPrimeiroNome);
        editSobrenome = view.findViewById(R.id.editSobrenome);
        editSobrenome = view.findViewById(R.id.editSobrenome);
        editCpf = view.findViewById(R.id.editCpf);
        editTelefone = view.findViewById(R.id.editTelefone);
        spinner = view.findViewById(R.id.spinner);

        btnVoltar = view.findViewById(R.id.btnVoltar);
        btnLimpar = view.findViewById(R.id.btnLimpar);
        btnSalvar = view.findViewById(R.id.btnSalvar);
        Log.d("2", "2 teste");
        Bundle args = getArguments();
        if (args != null) {
            int pessoaId = args.getInt("id", -1);

        if (pessoaId != -1) {
            Pessoa pessoa = controller.getPessoaById(pessoaId);

            editPrimeiroNome.setText(pessoa.getPrimeiroNome());
            editSobrenome.setText(pessoa.getSobrenome());
            editTelefone.setText(pessoa.getTelefone());
            editCpf.setText(pessoa.getCpf());

            int generoIndex = tiposDeGenero.indexOf(pessoa.getGenero());
            if (generoIndex != -1) {
                spinner.setSelection(generoIndex);
            }
        }
        }
            Log.d("3", "3 teste");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,
                controller.dadosParaSpinner());
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(adapter);

        int generoIndex = tiposDeGenero.indexOf(pessoa.getGenero());

        if (generoIndex != -1) {
            spinner.setSelection(generoIndex);
        }
            Log.d("4", "4 teste");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pessoa.setGenero((String) parent.getItemAtPosition(position));
                selectedGenero = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
            Log.d("CadastroFragment", "btnSalvar clicked");
        btnLimpar.setOnClickListener(view -> {
            editPrimeiroNome.setText("");
            editSobrenome.setText("");
            editTelefone.setText("");
            editCpf.setText("");

            controller.limpar();
        });

        btnVoltar.setOnClickListener(view -> {

            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);

        });

        btnSalvar.setOnClickListener(view -> {

            pessoa.setPrimeiroNome(editPrimeiroNome.getText().toString());
            pessoa.setSobrenome(editSobrenome.getText().toString());
            pessoa.setTelefone(editTelefone.getText().toString());
            pessoa.setCpf(editCpf.getText().toString());
            pessoa.setGenero(selectedGenero);

            Toast.makeText(getActivity(), "Pessoa Cadastrada", Toast.LENGTH_LONG).show();

            controller.salvar(pessoa);

            editPrimeiroNome.setText("");
            editSobrenome.setText("");
            editTelefone.setText("");
            editCpf.setText("");

        });
        return view;
    }
}