package Integracion;

import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import Negocio.Campo;
import Negocio.Centro;

public class CentroDB {
    private String myCol = "Centros";
    private String myColCampos = "Campos";
    private String myNombre = "nombre";
    private String myLocalizacion = "localizacion";
    private String myListaId = "lista";
    private Query query;
    private Campo campo;

    public ArrayList<Centro> obtenerCentros(String filtro_busqueda, String texto_busqueda,CentroCallback callback) {
        ArrayList<Centro> listaCentros = new ArrayList<>();
        query = SingletonDataBase.getInstance().getDB().collection(myCol).orderBy(filtro_busqueda, Query.Direction.ASCENDING);
            if (!filtro_busqueda.equals("lista")) {
                Log.d("PRUEBA", "A: " + filtro_busqueda + ", " + texto_busqueda);
                query = query.startAt(texto_busqueda).endAt(texto_busqueda + "~");
            }

            // Consulta para obtener todos los documentos de la colección "Centros"
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                            if (filtro_busqueda.equals("lista")) { //No funciona el obtener campos por lo que no puedo obtener deportes
                                campo.obtenerCampos(idCampos, new CampoDB.Callback() {
                                    @Override
                                    public void onSuccess(ArrayList<Campo> campos) {
                                        for (int i = 0; i < campos.size(); i++) {
                                            Log.d("PRUEBA", "D; "+ campos.get(i).getDeporte());
                                            if (texto_busqueda == campos.get(i).getDeporte()) {
                                                Centro centro = new Centro(nombre, localizacion, idCampos);
                                                listaCentros.add(centro);
                                            }
                                        }
                                    }
                                    @Override
                                    public void onError(Exception e) {
                                        // Maneja errores aquí
                                    }
                                });
                            }
                            else {
                                // Crea un objeto Centro y agrégalo a la lista
                                Centro centro = new Centro(nombre, localizacion, idCampos);
                                listaCentros.add(centro);
                            }
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
