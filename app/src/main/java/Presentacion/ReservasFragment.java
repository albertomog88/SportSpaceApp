package Presentacion;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.util.ArrayList;
import Integracion.CentroDB;
import Negocio.Centro;
import es.ucm.fdi.sportspaceapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Long longitud, latitud;

    private RecyclerView recyclerViewCentros;
    private Centro centro;
    private CentroAdapter centroAdapter;
    private ArrayList<Centro> listaCentros;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    public ReservasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Reservas.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservasFragment newInstance(String param1, String param2) {
        ReservasFragment fragment = new ReservasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializar FusedLocationProviderClient

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void obtenerUbicacionYCentros() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            double latitud = location.getLatitude();
                            double longitud = location.getLongitude();

                            // Ahora llama a obtenerCentros
                            centro.obtenerCentros(latitud, longitud, new CentroDB.CentroCallback() {
                                @Override
                                public void onCentrosObtenidos(ArrayList<Centro> centros) {
                                    // Actualiza la lista de centros y notifica al adaptador
                                    centroAdapter.setListaCentros(centros);
                                    centroAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(Exception e) {
                                    // Maneja errores aquí
                                }
                            });
                        } else {
                            // Manejar caso donde location == null
                        }
                    })
                    .addOnFailureListener(getActivity(), e -> {
                        // Manejar fallo al obtener la ubicación
                    });
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reservas, container, false);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        SearchView searchView = rootView.findViewById(R.id.search_view);
        // Configurar SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                centroAdapter.getFilter().filter(newText);
                return true;
            }
        });
        // Configurar el RecyclerView
        recyclerViewCentros = rootView.findViewById(R.id.recyclerViewCentros);
        recyclerViewCentros.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Inicializar el adaptador con una lista vacía
        listaCentros = new ArrayList<>();
        centroAdapter = new CentroAdapter(listaCentros, getContext());
        recyclerViewCentros.setAdapter(centroAdapter);

        // Llama a la función obtenerCentros y actualiza el adaptador cuando se complete la consulta
        centro = new Centro();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());

        // Asegúrate de que tienes los permisos antes de llamar a obtenerUbicacionYCentros
        obtenerUbicacionYCentros();

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El permiso fue concedido, puedes hacer la operación que requiere el permiso
                obtenerUbicacionYCentros();
            } else {
                // El permiso fue denegado, maneja adecuadamente la negación de este permiso
            }
        }
    }

}