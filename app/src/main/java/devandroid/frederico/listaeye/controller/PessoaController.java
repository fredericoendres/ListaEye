package devandroid.frederico.listaeye.controller;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import devandroid.frederico.listaeye.database.ListaEyeDB;
import devandroid.frederico.listaeye.model.Pessoa;
import devandroid.frederico.listaeye.view.CadastroActivity;

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
    SharedPreferences.Editor listaVip;
    public static final String NOME_PREFERENCES = "pref_listavip";

    public PessoaController(CadastroActivity mainActivity){
        super(mainActivity);
        preferences =
                mainActivity.getSharedPreferences(NOME_PREFERENCES, 0);
        listaVip = preferences.edit();
    }

    public void salvar(Pessoa pessoa) {
        ContentValues dados = new ContentValues();

        Log.d("MVC_Controller", "Salvo: "+pessoa.toString());

        listaVip.putString("primeiroNome", pessoa.getPrimeiroNome());
        listaVip.putString("sobrenome", pessoa.getSobrenome());
        listaVip.putString("genero", pessoa.getGenero());
        listaVip.putString("telefone", pessoa.getTelefone());
        listaVip.putString("cpf", pessoa.getCpf());
        listaVip.apply();

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

        listaVip.clear();
        listaVip.apply();

    }

}
