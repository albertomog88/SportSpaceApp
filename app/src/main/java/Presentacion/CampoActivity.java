package Presentacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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

    private TextView tituloCampo, fecha, disponibles;

    private Button reservarButton;
    private RecyclerView recyclerView;
    private HorarioAdapter horarioAdapter;
    private ArrayList<Horario> listaHorarios;
    private Campo campo;
    private String idCampo;

    private FirebaseUser usuarioActual ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campo);

        Intent intent = getIntent();
        String nombreCampo = getIntent().getStringExtra("nombre_campo");
        usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
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

        disponibles = findViewById(R.id.tituloHorario);



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
                disponibles.setText("Numero de horas disponibles "+listaHorarios.size());
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



        //Controlamos que el usuario este logeado
        if(usuarioActual != null){
            String email = usuarioActual.getEmail();
            List<Horario> horariosSeleccionados = horarioAdapter.obtenerHorariosSeleccionados();
            String horas="";
            for (Horario elemento : horariosSeleccionados) {
                horas+=elemento.getHora()+ " ";
            }
            showDialog("Resera realizada con exito",
                    "Usuario: "+email+"\n"+
                            tituloCampo.getText().toString()+"\n"+
                            "Fecha: "+fecha.getText().toString()+"\n"+
                            "Horas: "+horas);

        }
        else{
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

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
        //Evitamos que pueda reservar en una fecha anterior que la actual.
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    private void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Código a ejecutar cuando se hace clic en el botón "Aceptar"String email = ;
                        campo.insertarReserva(usuarioActual.getEmail(), idCampo, fecha.getText().toString(), horarioAdapter.obtenerHorariosSeleccionados());
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Código a ejecutar cuando se hace clic en el botón "Cancelar"
                        finish();
                    }
                })
                .show();
    }
}