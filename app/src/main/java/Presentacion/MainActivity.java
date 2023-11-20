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

public class MainActivity extends AppCompatActivity {


    private EditText et_correo, et_passw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_correo = findViewById(R.id.editTextTextEmailAddress);
        et_passw = findViewById(R.id.editTextTextPassword);
    }


    public void toRegistro(View v){
        startActivity(new Intent(this, Inicio.class));
    }

    public void toInicio(View v){
        String email = et_correo.getText().toString();
        String pass = et_passw.getText().toString();
        Usuario.comprobarCorreContra(email, pass, new UsuarioDB.Callback() {
            @Override
            public void onCallback(boolean isMatch) {
                if (isMatch) {
                    // La contraseña es correcta, iniciar nueva actividad
                    Intent intent = new Intent(MainActivity.this, Inicio.class);
                    startActivity(intent);
                } else {
                    // Contraseña incorrecta, mostrar mensaje
                    Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
        });

        startActivity(new Intent(this, RegistroActivity.class));
    }
}
