package Presentacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Integracion.CampoDB;
import Negocio.Campo;
import Negocio.Centro;
import es.ucm.fdi.sportspaceapp.R;

public class CentroActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCampos;
    private CampoAdapter campoAdapter;
    private ArrayList<Campo> listaCampos;
    private TextView tituloCentro;
    private Campo campo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centro); // Asegúrate de que este sea el nombre correcto de tu layout
        Intent intent = getIntent();
        String nombreCentro = getIntent().getStringExtra("centro");
        ArrayList<String> camposID = getIntent().getStringArrayListExtra("camposID");
        tituloCentro = findViewById(R.id.tituloCentro);
        recyclerViewCampos = findViewById(R.id.recyclerViewCampos);

        // Configurar el RecyclerView
        recyclerViewCampos.setLayoutManager(new LinearLayoutManager(this));
        campo = new Campo();
        listaCampos = new ArrayList<>();
        campoAdapter = new CampoAdapter(listaCampos, this);
        recyclerViewCampos.setAdapter(campoAdapter);

        // Establecer el título (puedes modificarlo para que reciba el nombre del centro desde un Intent, por ejemplo)
        tituloCentro.setText(nombreCentro);

        Centro centro = new Centro();
        campo.obtenerCampos(camposID, new CampoDB.Callback() {

            @Override
            public void onSuccess(ArrayList<Campo> campos) {
                listaCampos.clear();
                listaCampos.addAll(campos);
                campoAdapter.notifyDataSetChanged();
            }


            @Override
            public void onError(Exception e) {
                // Maneja errores aquí
            }
        });
    }


}
