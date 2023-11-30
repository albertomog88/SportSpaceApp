package Integracion;

import android.location.Location;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import Negocio.Centro;

public class CentroDB {
    private String myCol = "Centros";
    private String myNombre = "nombre";
    private String myLocalizacion = "localizacion";
    private String myListaId = "lista";
    private String myLatitud = "latitud";
    private String myLongitud = "longitud";
    private String distancia;

    public void obtenerCentros(double userLat, double userLng, CentroCallback callback) {
        ArrayList<Centro> listaCentros = new ArrayList<>();

        SingletonDataBase.getInstance().getDB().collection("Centros").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Accede a los datos de cada documento
                            String nombre = document.getString("nombre");
                            Number numLatitud = (Number) document.get("latitud");
                            Number numLongitud = (Number) document.get("longitud");

                            double latitud = numLatitud != null ? numLatitud.doubleValue() : 0.0;
                            double longitud = numLongitud != null ? numLongitud.doubleValue() : 0.0;
                            // Suponiendo que idCampos y localizacion son otros atributos que necesitas
                            ArrayList<String> idCampos = (ArrayList<String>) document.get("lista");
                            String localizacion = document.getString("localizacion");

                            Centro centro = new Centro(nombre, localizacion, idCampos, latitud, longitud);

                            // Calcular y establecer la distancia
                            double distancia = calcularDistancia(userLat, userLng, latitud, longitud);
                            centro.setDistancia(distancia);

                            listaCentros.add(centro);
                        }

                        // Ordenar por distancia
                        Collections.sort(listaCentros, Comparator.comparingDouble(Centro::getDistancia));

                        callback.onCentrosObtenidos(listaCentros);
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    // Método auxiliar para calcular la distancia
    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        // Convierte la distancia a kilómetros y redondea a un decimal
        return Math.round(results[0] / 1000.0 * 10.0) / 10.0;
    }

    public interface CentroCallback {
        void onCentrosObtenidos(ArrayList<Centro> listaCentros);
        void onError(Exception e);
    }
}
