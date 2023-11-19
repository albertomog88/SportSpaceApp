package Presentacion;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import es.ucm.fdi.sportspaceapp.R;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
    }

    //Cierra la Sesion y te devuelve al Login
    public void cerrarSesion(View v){
        auth.signOut();
        finish();
        startActivity(new Intent(this, InicioSesionActivity.class));
    }
}
