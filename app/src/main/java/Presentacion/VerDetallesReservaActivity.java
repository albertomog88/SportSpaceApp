package Presentacion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import Integracion.ReservaBD;
import Negocio.Reserva;
import es.ucm.fdi.sportspaceapp.R;

public class VerDetallesReservaActivity extends AppCompatActivity {

    public static final String EXTRA_NOMBRE_CENTRO = "nombreCentro";
    public static final String EXTRA_FECHA = "fecha";

    private String nombreCentro;
    private String fecha;
    private String hora;

    private String nombreCampo;
    private Reserva reserva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_detalles_reserva);

        // Recibir los datos pasados a través del Intent
        Intent intent = getIntent();
        nombreCentro = intent.getStringExtra(EXTRA_NOMBRE_CENTRO);
        fecha = intent.getStringExtra(EXTRA_FECHA);
        hora = intent.getStringExtra("hora");
        nombreCampo = intent.getStringExtra("nombreCampo");

        // Configurar las vistas
        TextView textViewNombreCentro = findViewById(R.id.textViewNombreCentroDetalles);
        TextView textViewFecha = findViewById(R.id.textViewFechaDetalles);
        TextView textViewNombreCampo = findViewById(R.id.textViewNombre_Campo);
        textViewNombreCentro.setText(nombreCentro);
        textViewFecha.setText(fecha + " - " + hora);
        textViewNombreCampo.setText(nombreCampo);
        reserva = new Reserva();
        // Configurar el botón para cancelar la reserva
        Button btnCancelar = findViewById(R.id.btnCancelarReserva);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
                String idUsuario = usuarioActual.getEmail();
                String idCampo = intent.getStringExtra("idCampo");
                String hora = intent.getStringExtra("hora");
                reserva.eliminarReserva(idUsuario, idCampo, fecha, hora, new ReservaBD.Callback() {
                    @Override
                    public void onSuccess(ArrayList<Reserva> reservas) {
                        Toast.makeText(VerDetallesReservaActivity.this,"Se ha eliminado correctamente tu reserva",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(VerDetallesReservaActivity.this,"Error al eliminar tu reserva",Toast.LENGTH_LONG).show();
                    }
                });
                finish();
                startActivity(new Intent(VerDetallesReservaActivity.this, MainActivity.class));
            }
        });
    }
}