package devandroid.frederico.listaeye.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import devandroid.frederico.listaeye.MainActivity;
import devandroid.frederico.listaeye.R;
import devandroid.frederico.listaeye.controller.PessoaAdapter;
import devandroid.frederico.listaeye.database.ListaEyeDB;
import devandroid.frederico.listaeye.model.Pessoa;

public class CalendarioActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PessoaAdapter adapter;
    private ListaEyeDB listaVipDB;
    private Calendar selectedInicioDate;
    private Calendar selectedFimDate;

    ImageButton btnVoltar;

    private void showDateTimePickerDialog(boolean isInicio) {
        Calendar calendar = Calendar.getInstance();
        long currentTimestamp = calendar.getTimeInMillis();

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setSelection(currentTimestamp);

        builder.setCalendarConstraints(new com.google.android.material.datepicker.CalendarConstraints.Builder().setOpenAt(currentTimestamp)
                .build());

        final MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.setTimeInMillis(selection);

            List<Pessoa> pessoaList = null;
            if (isInicio) {
                selectedInicioDate = selectedCalendar;
                showDateTimePickerDialog(false);
            } else {
                selectedFimDate = selectedCalendar;
                pessoaList = listaVipDB.listarDadosData(selectedInicioDate, selectedFimDate);
                adapter.updateData(pessoaList);
            }

            adapter.updateData(pessoaList);

        });
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnVoltar = findViewById(R.id.btnVerMais);

        listaVipDB = new ListaEyeDB(this);
        selectedInicioDate = Calendar.getInstance();
        selectedFimDate = Calendar.getInstance();
        List<Pessoa> pessoaList = listaVipDB.listarDadosData(selectedInicioDate, selectedFimDate);

        ImageButton btnDataInicio = findViewById(R.id.btnDataInicio);
        ImageButton btnDataFim =  findViewById(R.id.btnDataFim);
        adapter = new PessoaAdapter(new ArrayList<>());

        btnDataInicio.setOnClickListener(v -> showDateTimePickerDialog(true));

        btnDataFim.setOnClickListener(v -> {
            if (selectedInicioDate != null) {
                showDateTimePickerDialog(false);
            } else {
                Toast.makeText(CalendarioActivity.this, "Selecione a data inicial antes", Toast.LENGTH_SHORT).show();
            }
        });

        btnVoltar.setOnClickListener(view -> {

            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);

        });

        adapter = new PessoaAdapter(pessoaList, pessoaId -> {
            Intent intent = new Intent(CalendarioActivity.this, CadastroActivity.class);
            intent.putExtra("id", pessoaId);
            startActivity(intent);
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
