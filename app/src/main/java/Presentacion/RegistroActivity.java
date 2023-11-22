package Presentacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import Integracion.UsuarioDB;
import Negocio.Usuario;
import es.ucm.fdi.sportspaceapp.R;

public class RegistroActivity extends AppCompatActivity {

    private EditText et_nombre, et_ape1, et_ape2, et_fechNac, et_correo, et_passw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        et_nombre = findViewById(R.id.ed_nombre);
        et_ape1 = findViewById(R.id.ed_ape1);
        et_ape2 = findViewById(R.id.ed_ape2);
        et_fechNac = findViewById(R.id.ed_fechNac);
        et_correo = findViewById(R.id.ed_correo);
        et_passw = findViewById(R.id.ed_pass);
    }

    public void Registrar(View v){

        String nombre = et_nombre.getText().toString();
        String ape1 = et_ape1.getText().toString();
        String ape2 = et_ape2.getText().toString();
        ape1 += ape2;
        String fecha = et_fechNac.getText().toString();
        String email = et_correo.getText().toString();
        String pass = et_passw.getText().toString();
        Usuario u = new Usuario(nombre, ape1,  email, pass, fecha);

        u.existe(new UsuarioDB.Callback() {
            @Override
            public void onCallback(boolean exists) {
                if (!exists) {
                    // Usuario no existe, proceder con el registro
                    u.guardar();
                    Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();

                    // Navegar a la vista de inicio
                    Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Opcional, para finalizar esta actividad y no volver a ella al presionar 'Atrás'

                } else {
                    // Usuario ya existe, limpiar campos y mostrar mensaje
                    Vaciar();
                    Toast.makeText(RegistroActivity.this, "El usuario ya existe", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void Vaciar(){
        et_correo.setText("");
    }

    public void toInicio(View v){
        startActivity(new Intent(this, LoginActivity.class));

    }
}