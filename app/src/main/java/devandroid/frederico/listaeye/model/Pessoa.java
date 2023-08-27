package devandroid.frederico.listaeye.model;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


@DatabaseTable
public class Pessoa {

    Pessoa pessoa;



    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "primeiroNome")
    @SerializedName("primeiroNome")
    private String primeiroNome;

    @DatabaseField(columnName = "sobrenome")
    private String sobrenome;

    @DatabaseField(columnName = "cpf")
    private String cpf;

    @DatabaseField(columnName = "genero")
    private String genero;

    @DatabaseField(columnName = "telefone")
    private String telefone;

    @DatabaseField(columnName = "dataRegistro")
    private Date dataRegistro;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Pessoa() {
    }
}
