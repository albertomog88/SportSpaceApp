package Presentacion;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.os.Bundle;
import android.view.MenuItem;

import Presentacion.ReservasFragment;
import es.ucm.fdi.sportspaceapp.R;

public class Inicio extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();
                if (itemId == R.id.nav_inicio) {
                    selectedFragment = new ReservasFragment();
                } else if (itemId == R.id.nav_reservas) {
                    selectedFragment = new ReservasFragment();
                } else if (itemId == R.id.nav_equipos) {
                    selectedFragment = new ReservasFragment();
                } else if (itemId == R.id.nav_perfil) {
                    selectedFragment = new ReservasFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }

                return true;
            }
        });

        // Cargar el fragmento inicial
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReservasFragment()).commit();
            bottomNav.setSelectedItemId(R.id.nav_inicio);
        }
    }

}