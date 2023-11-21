package Integracion;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Negocio.Centro;

public class CentroDB {
    private String myCol = "Centros";
    private String myNombre = "nombre";
    private String myLocalizacion = "localizacion";
    private String myListaId = "lista";

    public ArrayList<Centro> obtenerCentros(CentroCallback callback) {
        ArrayList<Centro> listaCentros = new ArrayList<>();
        // Consulta para obtener todos los documentos de la colección "Centros"
        SingletonDataBase.getInstance().getDB().collection(myCol).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // La consulta se completó exitosamente
                            QuerySnapshot querySnapshot = task.getResult();
                            // Lista para almacenar los centros recuperados
                            // Recorre los documentos en la colección
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Accede a los datos de cada documento
                                String nombre = document.getString(myNombre);
                                String localizacion = document.getString(myLocalizacion);
                                ArrayList<String> idCampos = (ArrayList<String>) document.get(myListaId);

                                // Crea un objeto Centro y agrégalo a la lista
                                Centro centro = new Centro(nombre, localizacion, idCampos);
                                listaCentros.add(centro);
                            }

                            // Llama al método de la interfaz cuando se hayan recuperado todos los centros
                            callback.onCentrosObtenidos(listaCentros);
                        } else {
                            // Llama al método de la interfaz en caso de error
                            callback.onError(task.getException());
                        }
                    }
                });
        return listaCentros;
    }
    public interface CentroCallback {
        void onCentrosObtenidos(ArrayList<Centro> listaCentros);
        void onError(Exception e);
    }
}
