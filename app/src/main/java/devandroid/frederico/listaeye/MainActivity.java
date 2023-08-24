package devandroid.frederico.listaeye;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import devandroid.frederico.listaeye.controller.PessoaAdapter;
import devandroid.frederico.listaeye.database.ListaEyeDB;
import devandroid.frederico.listaeye.fragments.CadastroFragment;
import devandroid.frederico.listaeye.fragments.CalendarioFragment;
import devandroid.frederico.listaeye.fragments.HojeFragment;
import devandroid.frederico.listaeye.model.Pessoa;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private ListaEyeDB listaVipDB;
    private PessoaAdapter adapter;


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.nav_home) {
            setTitle("Cadastrados hoje");
            fragmentManager.beginTransaction().replace(R.id.content_fragment, new HojeFragment()).commit();
        } else if (item.getItemId() == R.id.nav_cadastro) {
            setTitle("Cadastro de Clientes");
            fragmentManager.beginTransaction().replace(R.id.content_fragment, new CadastroFragment()).commit();
        } else if (item.getItemId() == R.id.nav_calendario) {
            setTitle("Selecione uma data");
            fragmentManager.beginTransaction().replace(R.id.content_fragment, new CalendarioFragment()).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.content_fragment, new HojeFragment()).commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        FloatingActionButton downloadButton = findViewById(R.id.download);
        // IMPLEMENTAR METODO PRA FAZER O DOWNLOAD DA LIST DE PESSOAS PARA EXCEL

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaVipDB = new ListaEyeDB(this);
        List<Pessoa> pessoaList = listaVipDB.listarDadosHoje();


        adapter = new PessoaAdapter(pessoaList, pessoaId -> {
            CadastroFragment fragment = new CadastroFragment();
            Bundle args = new Bundle();
            args.putInt("id", pessoaId);
            fragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_fragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });


        adapter.setOnDeleteClickListener(pessoa -> {
            Snackbar snackbar = Snackbar.make(recyclerView, "Pessoa a ser deletada", Snackbar.LENGTH_LONG)
                    .setAction("CONFIRMAR", view -> {
                        listaVipDB.deletarObjeto(pessoa);
                        pessoaList.remove(pessoa);
                        adapter.notifyDataSetChanged();
                    });
            snackbar.show();
        });

        recyclerView.setAdapter(adapter);

    }
}