package Integracion;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Negocio.Campo;
import Negocio.Centro;

public class CampoDB {
    private String myCol = "Campos";
    private String myID = "id";
    private String myNombre = "nombre";
    private String myDeporte = "deporte";
    private String listaDisponibilidad = "listaDisponibilida";

    public interface Callback {

        void onSuccess(ArrayList<Campo> campos);

        void onError(Exception e);
    }

    public void obtenerCampos(ArrayList<String> idCampos, Callback callback) {
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        ArrayList<Campo> listaCampos = new ArrayList<>();
        SingletonDataBase.getInstance().getDB().collection(myCol).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // La consulta se completó exitosamente
                            QuerySnapshot querySnapshot = task.getResult();
                            // Lista para almacenar los centros recuperados
                            // Recorre los documentos en la colección
                            int i = 0;
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                if(document.getString(myID).equals(idCampos.get(i))){
                                    // Accede a los datos de cada documento
                                    String nombre = document.getString(myNombre);
                                    String id = document.getString(myID);
                                    String deporte = document.getString(myDeporte);
                                    //ArrayList<String> idCampos = (ArrayList<String>) document.get(myListaId);

                                    // Crea un objeto Centro y agrégalo a la lista
                                    Campo campo = new Campo(id, nombre, deporte);
                                    listaCampos.add(campo);
                                }
                                i++;
                            }

                            // Llama al método de la interfaz cuando se hayan recuperado todos los centros
                            callback.onSuccess(listaCampos);
                        } else {
                            // Llama al método de la interfaz en caso de error
                            callback.onError(task.getException());
                        }
                    }
                });


    }
}
