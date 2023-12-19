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
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.util.ArrayList;
import Integracion.CentroDB;
import Negocio.Centro;
import es.ucm.fdi.sportspaceapp.R;

public class ReservasFragment extends Fragment {
    // Variables y constantes
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerViewCentros;
    private Centro centro;
    private CentroAdapter centroAdapter;
    private ArrayList<Centro> listaCentros;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    public ReservasFragment() {
        // Required empty public constructor
    }

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        centro = new Centro();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
    }

    private void obtenerUbicacionYCentros() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            fusedLocationClient.getCurrentLocation(locationRequest.getPriority(), null)
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            centro.obtenerCentros(location.getLatitude(), location.getLongitude(), new CentroDB.CentroCallback() {
                                @Override
                                public void onCentrosObtenidos(ArrayList<Centro> centros) {
                                    centroAdapter.setListaCentros(centros);
                                    centroAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(Exception e) {
                                    Toast.makeText(getActivity(), "Error al obtener la lista de los centros", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "No se pudo obtener la ubicación", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Fallo al obtener la ubicación: " + e.getMessage(), Toast.LENGTH_LONG).show());
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reservas, container, false);

        // Configuración del SearchView
        SearchView searchView = rootView.findViewById(R.id.search_view);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
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

        // Configuración del RecyclerView
        recyclerViewCentros = rootView.findViewById(R.id.recyclerViewCentros);
        recyclerViewCentros.setLayoutManager(new LinearLayoutManager(getActivity()));
        listaCentros = new ArrayList<>();
        centroAdapter = new CentroAdapter(listaCentros, getContext());
        recyclerViewCentros.setAdapter(centroAdapter);

        // Verificar y solicitar permisos, luego obtener ubicación y centros
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            obtenerUbicacionYCentros();
        }



        return rootView;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionYCentros();
            } else {
                Toast.makeText(getActivity(), "Permiso de ubicación denegado", Toast.LENGTH_LONG).show();
            }
        }
    }
}
