package es.ucm.fdi.sportspaceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        Toast.makeText(this, "Boton registro", Toast.LENGTH_SHORT).show();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "sportspaceapp", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();



        String nombre = et_nombre.getText().toString();
        String ape1 = et_ape1.getText().toString();
        String ape2 = et_ape2.getText().toString();
        String fechNac = et_fechNac.getText().toString();
        String correo = et_correo.getText().toString();
        String passw = et_passw.getText().toString();

        if(!nombre.isEmpty() && !ape1.isEmpty() && !ape2.isEmpty() && !fechNac.isEmpty() && !correo.isEmpty() && !passw.isEmpty()){
            ContentValues registro = new ContentValues();

            registro.put("nombre", nombre);
            registro.put("apellido1", ape1);
            registro.put("apellido2", ape2);
            registro.put("mail", correo);
            registro.put("fechNac", fechNac);
            registro.put("password", passw);

            database.insert("usuarios", null, registro);
            database.close();
            this.Vaciar();
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
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