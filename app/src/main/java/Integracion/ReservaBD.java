package Integracion;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Negocio.Campo;
import Negocio.Horario;
import Negocio.Reserva;

public class ReservaBD {
    private String myCol = "Reservas";
    private String myFecha = "fecha";
    private String myHora = "hora";
    private String myIdCampo = "idCampo";
    private String myIdUsuario = "idUsuario";

    public interface Callback {
        void onSuccess(ArrayList<Reserva> reservas);

        void onError(Exception e);
    }
    public void obtenerReservas(String idUsuario, Callback callback){

        SingletonDataBase.getInstance().getDB().collection("Reservas")
                .whereEqualTo("idUsuario", idUsuario)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Reserva> reservas = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reserva reserva = document.toObject(Reserva.class);
                            String idCampo = reserva.getIdCampo();

                            // Consulta adicional para obtener el nombre del centro
                            SingletonDataBase.getInstance().getDB().collection("Centros")
                                    .whereArrayContains("lista", idCampo)
                                    .get()
                                    .addOnCompleteListener(taskCentro -> {
                                        if (taskCentro.isSuccessful()) {
                                            for (QueryDocumentSnapshot docCentro : taskCentro.getResult()) {
                                                // Suponiendo que el nombre del centro está en un campo llamado "nombre"
                                                String nombreCentro = docCentro.getString("nombre");
                                                reserva.setCentro(nombreCentro);

                                                SingletonDataBase.getInstance().getDB().collection("Campos")
                                                        .whereEqualTo("id", idCampo)
                                                        .get()
                                                        .addOnCompleteListener(taskNombreCampo -> {
                                                                    if (taskCentro.isSuccessful()) {
                                                                        for (QueryDocumentSnapshot docCampo : taskCentro.getResult()) {
                                                                            Campo c = docCampo.toObject(Campo.class);
                                                                            reserva.setNombreCampo(c.getNombre());
                                                                            Log.d("ReservaDatos", reserva.getNombreCampo() + " ID: " + reserva.getIdCampo());
                                                                        }


                                                                    }});


                                            }
                                        }
                                        reservas.add(reserva);

                                        // Llama al callback cuando todas las reservas han sido procesadas
                                        if (reservas.size() == task.getResult().size()) {
                                            callback.onSuccess((ArrayList<Reserva>) reservas);
                                        }
                                    });
                        }
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }



    public void eliminarReserva(String idUsuario, String idCampo, String fecha, String hora) {

        // Buscar y eliminar el documento de reserva
        SingletonDataBase.getInstance().getDB().collection("Reservas")
                .whereEqualTo("idUsuario", idUsuario)
                .whereEqualTo("idCampo", idCampo)
                .whereEqualTo("fecha", fecha)
                .whereEqualTo("hora", hora)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String docId = document.getId();
                        // Eliminar el documento encontrado
                        SingletonDataBase.getInstance().getDB().collection("Reservas").document(docId).delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Actualizar la disponibilidad en la colección 'Campos'
                                    DocumentReference campoRef = SingletonDataBase.getInstance().getDB().collection("Campos").document(idCampo);
                                    campoRef.get().addOnSuccessListener(campoDoc -> {
                                        if (campoDoc.exists()) {
                                            Map<String, Object> datosCampo = campoDoc.getData();
                                            if (datosCampo != null) {
                                                Map<String, Map<String, Boolean>> disponibilidad = (Map<String, Map<String, Boolean>>) datosCampo.get("disponibilidad");
                                                if (disponibilidad != null && disponibilidad.containsKey(fecha)) {
                                                    Map<String, Boolean> horariosDisponibles = disponibilidad.get(fecha);
                                                    horariosDisponibles.put(hora, true); // Marcar como disponible
                                                    campoRef.update("disponibilidad", disponibilidad);
                                                }
                                            }
                                        }
                                    });
                                    // Manejar éxito, por ejemplo, mostrar un mensaje al usuario
                                })
                                .addOnFailureListener(e -> {
                                    // Manejar error al eliminar la reserva
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Manejar error en la consulta de reservas
                });
    }

}
