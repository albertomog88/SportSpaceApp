package Presentacion;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import Integracion.ReservaBD;
import Negocio.Reserva;
import es.ucm.fdi.sportspaceapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MisReservasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MisReservasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerViewReservas;
    private ReservaAdapter reservaAdapter;
    private ArrayList<Reserva> listaReservas;
    private String idUsuario;

    public MisReservasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MisReservasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MisReservasFragment newInstance(String param1, String param2) {
        MisReservasFragment fragment = new MisReservasFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_mis_reservas, container, false);
        FirebaseUser usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        String idUsuario = usuarioActual.getEmail();
        // Configurar el RecyclerView
        recyclerViewReservas = rootView.findViewById(R.id.recyclerViewReservas);
        recyclerViewReservas.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Inicializar el adaptador con una lista vacía
        listaReservas = new ArrayList<>();
        reservaAdapter = new ReservaAdapter(listaReservas, getContext());
        recyclerViewReservas.setAdapter(reservaAdapter);
        int spaceInPixels = 40; // Ajusta esto según tus necesidades
        recyclerViewReservas.addItemDecoration(new SpacesItemDecoration(spaceInPixels));

        // Llama a la función obtenerCentros y actualiza el adaptador cuando se complete la consulta
        Reserva reserva = new Reserva();
        reserva.obtenerReservas(idUsuario, new ReservaBD.Callback() {
            @Override
            public void onSuccess(ArrayList<Reserva> reservas) {
                // Actualiza la lista de centros y notifica al adaptador
                listaReservas.clear();
                listaReservas.addAll(reservas);
                reservaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getActivity(),"Error al obtener tus reservas",Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }
}