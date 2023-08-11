package devandroid.frederico.listaeye.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import devandroid.frederico.listaeye.model.Pessoa;

public class ListaEyeDB extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "listavip.db";
    public static final int DB_VERSION = 1;

    private Dao<Pessoa, Integer> pessoaDao;

    public ListaEyeDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Pessoa.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Pessoa.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Dao<Pessoa, Integer> getPessoaDao() throws SQLException {
        if (pessoaDao == null) {
            pessoaDao = DaoManager.createDao(getConnectionSource(), Pessoa.class);
        }
        return pessoaDao;
    }

    public void salvarObjeto(String tabela, Pessoa pessoa) {
        try {
            pessoa.setDataRegistro(new Date());
            getPessoaDao().create(pessoa);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pessoa> listarDadosHoje() {
        try {
            long tempoAtualMillis = System.currentTimeMillis();
            long vinteQuatroHoras = tempoAtualMillis - (24 * 60 * 60 * 1000);

            QueryBuilder<Pessoa, Integer> queryBuilder = getPessoaDao().queryBuilder();
            queryBuilder.where().ge("dataRegistro", new Date(vinteQuatroHoras));

            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Pessoa> listarDadosData(Calendar selectedDateInicio, Calendar selectedDateFim) {
        try {
            selectedDateInicio.set(Calendar.HOUR_OF_DAY, 0);
            selectedDateInicio.set(Calendar.MINUTE, 0);
            selectedDateInicio.set(Calendar.SECOND, 0);
            selectedDateInicio.set(Calendar.MILLISECOND, 0);

            selectedDateFim.set(Calendar.HOUR_OF_DAY, 23);
            selectedDateFim.set(Calendar.MINUTE, 59);
            selectedDateFim.set(Calendar.SECOND, 59);
            selectedDateFim.set(Calendar.MILLISECOND, 999);

            long startTimeMillis = selectedDateInicio.getTimeInMillis();
            long endTimeMillis = selectedDateFim.getTimeInMillis();

            QueryBuilder<Pessoa, Integer> queryBuilder = getPessoaDao().queryBuilder();
            queryBuilder.where().between("dataRegistro", new Date(startTimeMillis), new Date(endTimeMillis));

            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deletarObjeto(Pessoa pessoa) {
        try {
            getPessoaDao().delete(pessoa);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Pessoa getPessoaById(int pessoaId) {
        try {
            return getPessoaDao().queryForId(pessoaId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
