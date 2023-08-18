package devandroid.frederico.listaeye.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class CadastroActivity extends AppCompatActivity {

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        controller = new PessoaController(CadastroActivity.this);
        dados = controller.getListaDeDados();
        tiposDeGenero = controller.dadosParaSpinner();

        pessoa = new Pessoa();

        editPrimeiroNome = findViewById(R.id.editPrimeiroNome);
        editSobrenome = findViewById(R.id.editSobrenome);
        editSobrenome = findViewById(R.id.editSobrenome);
        editCpf = findViewById(R.id.editCpf);
        editTelefone = findViewById(R.id.editTelefone);
        spinner = findViewById(R.id.spinner);

        btnVoltar = findViewById(R.id.btnVerMais);
        btnLimpar = findViewById(R.id.btnLimpar);
        btnSalvar = findViewById(R.id.btnSalvar);

        int pessoaId = getIntent().getIntExtra("id", -1);
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                controller.dadosParaSpinner());
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(adapter);

        int generoIndex = tiposDeGenero.indexOf(pessoa.getGenero());

        if (generoIndex != -1) {
            spinner.setSelection(generoIndex);
        }

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

            Toast.makeText(CadastroActivity.this, "Pessoa Cadastrada", Toast.LENGTH_LONG).show();

            controller.salvar(pessoa);

            editPrimeiroNome.setText("");
            editSobrenome.setText("");
            editTelefone.setText("");
            editCpf.setText("");

        });

    }
}