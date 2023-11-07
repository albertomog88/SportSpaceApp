package es.ucm.fdi.sportspaceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void toRegistro(View v){
        startActivity(new Intent(this, RegistroActivity.class));


    }

    public void toInicio(View v){
        startActivity(new Intent(this, Inicio.class));


    }
}
