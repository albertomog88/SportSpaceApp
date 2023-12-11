package Presentacion;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import Integracion.UsuarioDB;
import Negocio.Usuario;
import es.ucm.fdi.sportspaceapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView mail, nombreApe, fechaNac;

    private Button cierreSesion;
    private FirebaseAuth mAuth;
    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_perfil, container, false);
        FirebaseUser usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        mAuth=FirebaseAuth.getInstance();
        mail = rootView.findViewById(R.id.mailUsu);
        mail.setText("Email: "+usuarioActual.getEmail());

        Usuario u = new Usuario();
        u.getUsuario(usuarioActual.getEmail(), new UsuarioDB.Callback() {
            @Override
            public void success(Usuario u) {
                nombreApe = rootView.findViewById(R.id.nombreApeUsu);
                nombreApe.setText("Nombre Completo: "+u.getNombre()+" "+u.getApellidos());

                fechaNac = rootView.findViewById(R.id.fechNacUsu);
                fechaNac.setText(("Fecha de Nacimiento: "+ u.getFecha()));
            }
            @Override
            public void onCallback(boolean exists) {}
            @Override
            public void onError(Exception e) {}
        });

        cierreSesion = rootView.findViewById(R.id.cierreSesion);

        cierreSesion.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                //OnCLick Stuff
                Log.d("Cierre de sesion", "Cerrando Sesion");
                mAuth.signOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        return rootView;
    }


}