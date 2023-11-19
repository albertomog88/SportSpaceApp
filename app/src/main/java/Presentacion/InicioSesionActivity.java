package Presentacion;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import es.ucm.fdi.sportspaceapp.R;

public class InicioSesionActivity extends AppCompatActivity {

    private EditText et_email, et_pass;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();

        et_email = findViewById(R.id.ed_email);
        et_pass = findViewById(R.id.ed_pass);
    }

    public void iniciarSesion(View v){
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
                        finish();
                        startActivity(new Intent(InicioSesionActivity.this, MainActivity.class));
                        Toast.makeText(InicioSesionActivity.this, "Inicio de sesion exitoso", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(InicioSesionActivity.this, "Error al iniciar sesion", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(InicioSesionActivity.this, "Error al iniciar sesion", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void toRegistro(View v){
        startActivity(new Intent(this, RegistroActivity.class));
    }

    //Comprueba si has iniciado sesion hace poco y te mete directamente en la aplicacion
    /*@Override
    protected void onStart() {
        super.onStart();
        FirebaseUser fu = auth.getCurrentUser();
        if (fu != null){
            startActivity(new Intent(InicioSesionActivity.this, MainActivity.class));
            finish();
        }
    }*/
}



