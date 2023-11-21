package Integracion;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Negocio.Campo;

public class CampoDB {
    private String myCol = "Campos";
    private String myNombre = "nombre";
    private String myDeporte = "deporte";
    private String listaDisponibilidad = "listaDisponibilida";

    public interface Callback {
        void onSuccess(List<Campo> campos);
        void onError(Exception e);
    }

    public void obtenerCamposPorIds(List<Integer> idCampos, Callback callback) {
        List<Campo> listaCampos = new ArrayList<>();

        // Consulta para obtener los campos cuyos IDs estén en la lista
        SingletonDataBase.getInstance().getDB().collection("Campos")
                .whereIn("idCampo", idCampos)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Convierte cada documento en un objeto Campo y agrégalo a la lista
                                Campo campo = document.toObject(Campo.class);
                                listaCampos.add(campo);
                            }
                            callback.onSuccess(listaCampos);
                        } else {
                            callback.onError(task.getException());
                        }
                    }
                });
    }
}
