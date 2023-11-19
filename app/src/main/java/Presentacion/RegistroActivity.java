package Presentacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

        if(!u.existe()){
            Log.println(Log.DEBUG, "Existe Usuario", "Sí");
            u.guardar();
            this.Vaciar();
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show();
        }else{
            Log.println(Log.DEBUG, "Existe Usuario", "No");
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    private void Vaciar(){
        et_nombre.setText("");
        et_ape1.setText("");
        et_ape2.setText("");
        et_fechNac.setText("");
        et_correo.setText("");
        et_passw.setText("");
    }

    public void toInicio(View v){
        startActivity(new Intent(this, Inicio.class));

    }
}