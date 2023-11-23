package Presentacion;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import es.ucm.fdi.sportspaceapp.R;

public class LoginActivity extends AppCompatActivity {
    private EditText et_email, et_pass;
    private CheckBox showPasswordCheckBox;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.ed_email);
        et_pass = findViewById(R.id.ed_pass);
        showPasswordCheckBox = findViewById(R.id.checkBoxShowPassword);

        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Cambiar la visibilidad de la contraseña según el estado del CheckBox
                if (isChecked) {
                    // Hacer la contraseña visible
                    et_pass.setTransformationMethod(null);
                } else {
                    // Ocultar la contraseña
                    et_pass.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
    }

    public void ToIniciarSesion(View v){
        String email = et_email.getText().toString();
        String pass = et_pass.getText().toString();

        if(email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Debes rellenar los datos", Toast.LENGTH_SHORT).show();
        }
        else {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        //Si Inicia Sesion Correctamente le lleva al MainActivity
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        Toast.makeText(LoginActivity.this, "Inicio de sesion exitoso", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Error al iniciar sesion", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "Error al iniciar sesion", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void toRegistro(View v){
        finish();
        startActivity(new Intent(this, RegistroActivity.class));
    }

    //Comprueba si has iniciado sesion hace poco y te mete directamente en la aplicacion
    protected void onStart() {
        super.onStart();
        auth.signOut();
    }
}