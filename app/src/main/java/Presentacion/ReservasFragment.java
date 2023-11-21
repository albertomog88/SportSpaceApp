package Presentacion;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private RecyclerView recyclerViewCentros;
    private CentroAdapter centroAdapter;
    private ArrayList<Centro> listaCentros;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reservas, container, false);

        // Configurar el RecyclerView
        recyclerViewCentros = rootView.findViewById(R.id.recyclerViewCentros);
        recyclerViewCentros.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Inicializar el adaptador con una lista vacía
        listaCentros = new ArrayList<>();
        centroAdapter = new CentroAdapter(listaCentros, getContext());
        recyclerViewCentros.setAdapter(centroAdapter);

        // Llama a la función obtenerCentros y actualiza el adaptador cuando se complete la consulta
        Centro centro = new Centro();
        centro.obtenerCentros(new CentroDB.CentroCallback() {
            @Override
            public void onCentrosObtenidos(ArrayList<Centro> centros) {
                // Actualiza la lista de centros y notifica al adaptador
                listaCentros.clear();
                listaCentros.addAll(centros);
                centroAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                // Maneja errores aquí
            }
        });

        return rootView;
    }
}