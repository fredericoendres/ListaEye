package devandroid.frederico.listaeye.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import devandroid.frederico.listaeye.R;
import devandroid.frederico.listaeye.controller.PessoaAdapter;
import devandroid.frederico.listaeye.database.ListaEyeDB;
import devandroid.frederico.listaeye.model.Pessoa;


public class CalendarioFragment extends Fragment {

    View view;
    private RecyclerView recyclerView;
    private PessoaAdapter adapter;
    private ListaEyeDB listaEyeDB;
    private Calendar selectedInicioDate;
    private Calendar selectedFimDate;

    FragmentManager fragmentManager;

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
                pessoaList = listaEyeDB.listarDadosData(selectedInicioDate, selectedFimDate);
                adapter.updateData(pessoaList);
            }

            adapter.updateData(pessoaList);

        });
        datePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER_TAG");
    }

    public CalendarioFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendario, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnVoltar = view.findViewById(R.id.btnVoltar);

        listaEyeDB = new ListaEyeDB(getContext());
        selectedInicioDate = Calendar.getInstance();
        selectedFimDate = Calendar.getInstance();
        List<Pessoa> pessoaList = listaEyeDB.listarDadosData(selectedInicioDate, selectedFimDate);

        ImageButton btnDataInicio = view.findViewById(R.id.btnDataInicio);
        ImageButton btnDataFim =  view.findViewById(R.id.btnDataFim);
        adapter = new PessoaAdapter(new ArrayList<>());

        btnDataInicio.setOnClickListener(v -> showDateTimePickerDialog(true));

        btnDataFim.setOnClickListener(v -> {
            if (selectedInicioDate != null) {
                showDateTimePickerDialog(false);
            } else {
                Toast.makeText(getActivity(), "Selecione a data inicial antes", Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new PessoaAdapter(pessoaList, pessoaId -> {
            CadastroFragment fragment = new CadastroFragment();
            Bundle args = new Bundle();
            args.putInt("id", pessoaId);
            fragment.setArguments(args);

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.calendario_fragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        adapter.setOnDeleteClickListener(pessoa -> {
            Snackbar snackbar = Snackbar.make(recyclerView, "Pessoa a ser deletada", Snackbar.LENGTH_LONG)
                    .setAction("CONFIRMAR", view -> {
                        listaEyeDB.deletarObjeto(pessoa);
                        pessoaList.remove(pessoa);
                        adapter.updateData(pessoaList);
                    });
            snackbar.show();
        });
        recyclerView.setAdapter(adapter);
        return view;
    }
}