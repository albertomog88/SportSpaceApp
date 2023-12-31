package Presentacion;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import Integracion.UsuarioDB;
import Negocio.Usuario;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import es.ucm.fdi.sportspaceapp.R;

public class RegistroActivity extends AppCompatActivity {

    private EditText et_nombre, et_ape1, et_ape2, et_fechNac, et_correo, et_passw, et_passw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        et_nombre = findViewById(R.id.ed_nombre);
        et_ape1 = findViewById(R.id.ed_ape1);
        et_ape2 = findViewById(R.id.ed_ape2);
        et_fechNac = findViewById(R.id.ed_fechNac);
        et_fechNac.setFocusable(false);
        et_fechNac.setFocusableInTouchMode(false);
        et_fechNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePickerDialog();
            }
        });
        et_correo = findViewById(R.id.ed_correo);
        et_passw = findViewById(R.id.ed_pass);
        et_passw2 = findViewById(R.id.ed_pass2);
    }

    public void Registrar(View v){

        String nombre = et_nombre.getText().toString();
        String ape1 = et_ape1.getText().toString();
        String ape2 = et_ape2.getText().toString();
        ape1 +=" "+ ape2;
        String fechNac = et_fechNac.getText().toString();

        String email = et_correo.getText().toString();
        String pass = et_passw.getText().toString();
        String pass2 = et_passw2.getText().toString();


        if(validarCampos(nombre, ape1, ape2, fechNac, email, pass, pass2)){
            Usuario u = new Usuario(nombre, ape1,  email, pass, fechNac);

            u.existe(new UsuarioDB.Callback() {
                @Override
                public void success(Usuario u) {}
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
                @Override
                public void onError(Exception e) {}
            });
        }
    }

    private void mostrarDatePickerDialog() {
        // Obtener la fecha actual para mostrar en el DatePickerDialog
        Calendar calendar = Calendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int día = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear e mostrar el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Cuando el usuario selecciona una fecha, actualizar el texto del EditText
                        String fechaSeleccionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        et_fechNac.setText(fechaSeleccionada);
                    }
                }, año, mes, día);

        // Configurar la fecha mínima y máxima (opcional)
        // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Para establecer la fecha mínima al día actual
        // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // Para establecer la fecha máxima al día actual

        datePickerDialog.show();
    }

    private boolean validarCampos(String nombre, String apellido1, String apellido2, String fechaNacimiento, String correo, String passw, String passw2) {
        // Verificar que todos los campos estén completos
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido1) ||
                TextUtils.isEmpty(apellido2) || TextUtils.isEmpty(fechaNacimiento) ||
                TextUtils.isEmpty(correo) || TextUtils.isEmpty(passw)) {

            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificar el formato de la fecha de nacimiento
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            Date date = dateFormat.parse(fechaNacimiento);
        } catch (ParseException e) {
            Toast.makeText(this, "Formato de fecha de nacimiento incorrecto (dd/MM/yyyy)", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificar el formato del correo electrónico
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Formato de correo electrónico incorrecto", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!passw.equals(passw2)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar la seguridad de la passw - Desactivado por el momento
        if (!esContraseñaSegura(passw)) {
            Toast.makeText(this, "La password no cumple con los criterios de seguridad", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean esContraseñaSegura(String contraseña) {
        // Criterios de seguridad para la contraseña:
        // - Al menos 8 caracteres de longitud
        // - Al menos una letra mayúscula
        // - Al menos una letra minúscula
        // - Al menos un número
        // - Al menos un carácter especial

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contraseña);

        return matcher.matches();
    }

    private void Vaciar(){
        et_correo.setText("");
    }

    public void toInicio(View v){
        finish();
        startActivity(new Intent(this, LoginActivity.class));

    }
}