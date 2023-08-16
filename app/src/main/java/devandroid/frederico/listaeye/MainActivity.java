package devandroid.frederico.listaeye;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import devandroid.frederico.listaeye.controller.PessoaAdapter;
import devandroid.frederico.listaeye.database.ListaEyeDB;
import devandroid.frederico.listaeye.model.Pessoa;
import devandroid.frederico.listaeye.view.CadastroActivity;
import devandroid.frederico.listaeye.view.CalendarioActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private ListaEyeDB listaVipDB;
    private PessoaAdapter adapter;


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_calendario) {
            Intent intent = new Intent(this, CalendarioActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, CadastroActivity.class);
            startActivity(intent);
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
            Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
            intent.putExtra("id", pessoaId);
            startActivity(intent);
        });

        adapter.setOnDeleteClickListener(pessoa -> {
            Snackbar snackbar = Snackbar.make(recyclerView, "Pessoa a ser deletada", Snackbar.LENGTH_LONG)
                    .setAction("CONFIRMAR", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listaVipDB.deletarObjeto(pessoa);
                            pessoaList.remove(pessoa);
                            adapter.notifyDataSetChanged();
                        }
                    });
            snackbar.show();
        });

        recyclerView.setAdapter(adapter);

    }
}