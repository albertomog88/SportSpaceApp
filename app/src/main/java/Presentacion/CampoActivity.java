package Presentacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import Integracion.CampoDB;
import Negocio.Campo;
import Negocio.Horario;
import es.ucm.fdi.sportspaceapp.R;

public class CampoActivity extends AppCompatActivity {

    private TextView tituloCampo;
    private TextView fecha;
    private Button reservarButton;
    private RecyclerView recyclerView;
    private HorarioAdapter horarioAdapter;
    private ArrayList<Horario> listaHorarios;
    private Campo campo;
    private String idCampo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campo);
        Intent intent = getIntent();
        String nombreCampo = getIntent().getStringExtra("nombre_campo");
        idCampo = getIntent().getStringExtra("id_campo"); // Asignar a la variable idCampo
        reservarButton = findViewById(R.id.butttonReservar);
        reservarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toReservar();
            }
        });
        recyclerView = findViewById(R.id.recyclerViewHorarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tituloCampo = findViewById(R.id.tituloCampo);
        tituloCampo.setText(nombreCampo);

        fecha = findViewById(R.id.fechaCampo); // Asegúrate de que este ID exista en tu layout
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePickerDialog();
            }
        });
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaActual = formatoFecha.format(calendario.getTime());
        fecha.setText(fechaActual);
        // Aquí deberías obtener tu lista de horarios de alguna forma
        listaHorarios = new ArrayList<Horario>(); // Implementa este método
        horarioAdapter = new HorarioAdapter(listaHorarios, this);
        recyclerView.setAdapter(horarioAdapter);
        campo = new Campo();

        campo.verificarYActualizarDisponibilidad(idCampo, fechaActual, new CampoDB.Callback() {


            @Override
            public void success(List<Horario> horarios) {
                Collections.sort(horarios, new Comparator<Horario>() {
                    @Override
                    public int compare(Horario h1, Horario h2) {
                        return convertirHoraAValor(h1.getHora()) - convertirHoraAValor(h2.getHora());
                    }

                    private int convertirHoraAValor(String hora) {
                        String[] partes = hora.split(":");
                        int horas = Integer.parseInt(partes[0]);
                        int minutos = Integer.parseInt(partes[1]);
                        return horas * 60 + minutos; // Convertir a minutos totales
                    }
                });
                listaHorarios.clear();
                listaHorarios.addAll(horarios);
                horarioAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(ArrayList<Campo> campos) {

            }


            @Override
            public void onError(Exception e) {
                // Maneja errores aquí
            }
        });
    }


    public void toReservar() {
        FirebaseUser usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        String email = usuarioActual.getEmail();
        List<Horario> horariosSeleccionados = horarioAdapter.obtenerHorariosSeleccionados();
        campo.insertarReserva(email, idCampo, fecha.getText().toString(), horariosSeleccionados);
        //campo.actualizarDisponibilidad(idCampo, fecha.getText().toString(), horariosSeleccionados);
        finish();
    }

    private void actualizarListaHorarios(String idCampo, String fecha) {
        campo.verificarYActualizarDisponibilidad(idCampo, fecha, new CampoDB.Callback() {
            @Override
            public void success(List<Horario> horarios) {
                listaHorarios.clear();
                listaHorarios.addAll(horarios);
                horarioAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(ArrayList<Campo> campos) {
                // No necesario para este caso
            }

            @Override
            public void onError(Exception e) {
                // Maneja errores aquí
            }
        });
    }

    private void mostrarDatePickerDialog() {
        Calendar calendario = Calendar.getInstance();
        int year = calendario.get(Calendar.YEAR);
        int month = calendario.get(Calendar.MONTH);
        int day = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendario.set(Calendar.YEAR, year);
                        calendario.set(Calendar.MONTH, month);
                        calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String fechaSeleccionada = formatoFecha.format(calendario.getTime());
                        fecha.setText(fechaSeleccionada); // Muestra la fecha seleccionada en el TextView
                        actualizarListaHorarios(idCampo, fechaSeleccionada);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }
}