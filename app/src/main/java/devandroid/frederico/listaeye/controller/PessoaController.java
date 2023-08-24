package devandroid.frederico.listaeye.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import devandroid.frederico.listaeye.database.ListaEyeDB;
import devandroid.frederico.listaeye.model.Pessoa;

public class PessoaController extends ListaEyeDB {

    public List<String> getGeneroOptions() {
        List<String> generoOptions = new ArrayList<>();
        generoOptions.add("Masculino");
        generoOptions.add("Feminino");
        generoOptions.add("Outro");
        return generoOptions;
    }

    public ArrayList<String> dadosParaSpinner() {
        List<String> generoOptions = getGeneroOptions();

        ArrayList<String> dados = new ArrayList<>();
        dados.addAll(generoOptions);

        return dados;
    }

    SharedPreferences preferences;
    SharedPreferences.Editor listaEye;
    public static final String NOME_PREFERENCES = "pref_listavip";

    private Context context;
    public PessoaController(Context context) {
        super(context);

        if (context == null) {
            throw new IllegalArgumentException("Context nao pode ser nulo");
        }

        this.context = context;
        preferences = context.getSharedPreferences(NOME_PREFERENCES, 0);
        listaEye = preferences.edit();
    }

    public void salvar(Pessoa pessoa) {
        ContentValues dados = new ContentValues();

        Log.d("MVC_Controller", "Salvo: "+pessoa.toString());

        listaEye.putString("primeiroNome", pessoa.getPrimeiroNome());
        listaEye.putString("sobrenome", pessoa.getSobrenome());
        listaEye.putString("genero", pessoa.getGenero());
        listaEye.putString("telefone", pessoa.getTelefone());
        listaEye.putString("cpf", pessoa.getCpf());
        listaEye.apply();

        dados.put("primeiroNome", pessoa.getPrimeiroNome());
        dados.put("sobrenome", pessoa.getSobrenome());
        dados.put("telefone", pessoa.getTelefone());
        dados.put("cpf", pessoa.getCpf());
        dados.put("generoInformado", pessoa.getGenero());

        salvarObjeto(pessoa);

    }

    private void salvarObjeto(Pessoa pessoa) {
        salvarObjeto("Lista", pessoa);
    }

    public List<Pessoa> getListaDeDados(){
        return listarDadosHoje();
    }

    public void limpar(){

        listaEye.clear();
        listaEye.apply();

    }

}
