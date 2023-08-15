package devandroid.frederico.listaeye.view;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;
import java.util.List;

import devandroid.frederico.listaeye.controller.PessoaAdapter;
import devandroid.frederico.listaeye.database.ListaEyeDB;
import devandroid.frederico.listaeye.model.Pessoa;

public class CalendarioActivity {

    private RecyclerView recyclerView;
    private PessoaAdapter adapter;
    private ListaEyeDB listaVipDB;
    private Calendar selectedInicioDate;
    private Calendar selectedFimDate;

    private void showDateTimePickerDialog(boolean isInicio) {
        Calendar calendar = Calendar.getInstance();
        long currentTimestamp = calendar.getTimeInMillis();

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setSelection(currentTimestamp);

        builder.setCalendarConstraints(new com.google.android.material.datepicker.CalendarConstraints.Builder().setOpenAt(currentTimestamp)
                .build());

        final MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
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
            }
        });
        datePicker.show();
    }

}
