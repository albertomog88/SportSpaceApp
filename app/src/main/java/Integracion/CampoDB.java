package Integracion;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Negocio.Campo;
import Negocio.Horario;

public class CampoDB {
    private String myCol = "Campos";
    private String myID = "id";
    private String myNombre = "nombre";
    private String myDeporte = "deporte";
    private String listaDisponibilidad = "listaDisponibilida";

    public interface Callback {
        void success(List<Horario> listaHorarios);
        void onSuccess(ArrayList<Campo> campos);

        void onError(Exception e);
    }

    public void obtenerCampos(ArrayList<String> idCampos, Callback callback) {
        ArrayList<Campo> listaCampos = new ArrayList<>();

        SingletonDataBase.getInstance().getDB().collection(myCol).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String id = document.getString(myID);
                            // Comprobar si el ID del documento está en la lista de IDs de campos
                            if (idCampos.contains(id)) {
                                // Accede a los datos de cada documento
                                String nombre = document.getString("nombre");
                                String deporte = document.getString("deporte");
                                Campo campo = new Campo(id, nombre, deporte);
                                listaCampos.add(campo);
                            }
                        }
                        // Llama al método onSuccess de la interfaz de callback
                        callback.onSuccess(listaCampos);
                    } else {
                        // Llama al método onError de la interfaz de callback
                        callback.onError(task.getException());
                    }
                });
    }

    public void verificarYActualizarDisponibilidad(String idBuscado, String fecha, Callback callback) {


        SingletonDataBase.getInstance().getDB().collection(myCol)
                .whereEqualTo("id", idBuscado)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Map<String, Map<String, Boolean>> disponibilidad = (Map<String, Map<String, Boolean>>) documentSnapshot.get("disponibilidad");
                            if (disponibilidad == null) {
                                disponibilidad = new HashMap<>();
                            }
                            List<Horario> listaHorarios = new ArrayList<>();

                            if (!disponibilidad.containsKey(fecha)) {
                                for (int i = 9; i <= 20; i++) {
                                    listaHorarios.add(new Horario(i + ":00", true));
                                }

                                Map<String, Boolean> horasMap = new HashMap<>();
                                for (Horario horario : listaHorarios) {
                                    horasMap.put(horario.getHora(), horario.getDisponible());
                                }

                                disponibilidad.put(fecha, horasMap);
                                documentSnapshot.getReference().update("disponibilidad", disponibilidad);
                            } else {
                                Map<String, Boolean> horas = disponibilidad.get(fecha);
                                for (Map.Entry<String, Boolean> entrada : horas.entrySet()) {
                                    if(entrada.getValue())
                                        listaHorarios.add(new Horario(entrada.getKey(), entrada.getValue()));
                                }
                            }

                            callback.success(listaHorarios);
                            return; // Procesa solo el primer documento que coincida
                        }
                    } else {
                        callback.onError(new Exception("Documento con el ID proporcionado no encontrado"));
                    }
                })
                .addOnFailureListener(e -> callback.onError(e));
    }

    public void insertarReserva(String idUsuario, String idCampo, String fecha, List<Horario> listaHoras){
        WriteBatch batch = SingletonDataBase.getInstance().getDB().batch();
        DocumentReference campoRef = SingletonDataBase.getInstance().getDB().collection(myCol).document(idCampo);

        for (Horario horario : listaHoras) {
            // Crear y agregar reservas al batch
            DocumentReference reservaRef = SingletonDataBase.getInstance().getDB().collection("Reservas").document();
            Map<String, Object> reserva = new HashMap<>();
            reserva.put("idUsuario", idUsuario);
            reserva.put("idCampo", idCampo);
            reserva.put("fecha", fecha);
            reserva.put("hora", horario.getHora());
            batch.set(reservaRef, reserva);

            // Preparar actualización de disponibilidad y agregar al batch
            FieldPath path = FieldPath.of("disponibilidad", fecha, horario.getHora());
            batch.update(campoRef, path, false);
        }

        // Ejecutar el batch
        batch.commit().addOnSuccessListener(aVoid -> {
            // Manejar éxito, todas las operaciones se completaron
            // Aquí puedes agregar alguna lógica si necesitas manejar el éxito de la operación
        }).addOnFailureListener(e -> {
            // Manejar error, alguna operación falló
            // Aquí puedes agregar manejo de errores, como mostrar un mensaje al usuario
        });
    }

    public void actualizarDisponibilidad(String idCampo, String fecha, List<Horario> listaHoras){
        for (Horario horario : listaHoras) {
            // Actualizar la disponibilidad del campo
            DocumentReference campoRef = SingletonDataBase.getInstance().getDB().collection(myCol).document(idCampo);
            campoRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Map<String, Object> datosCampo = documentSnapshot.getData();
                    if (datosCampo != null) {
                        // Aquí debes actualizar la disponibilidad en base a tu estructura de datos
                        // Por ejemplo, si tienes un mapa de fechas a disponibilidades:
                        Map<String, Map<String, Boolean>> disponibilidad = (Map<String, Map<String, Boolean>>) datosCampo.get("disponibilidad");
                        if (disponibilidad != null && disponibilidad.containsKey(fecha)) {
                            Map<String, Boolean> horariosDisponibles = disponibilidad.get(fecha);
                            horariosDisponibles.put(horario.getHora(), false); // Marcar como no disponible
                            disponibilidad.put(fecha, horariosDisponibles);
                        }
                        campoRef.update("disponibilidad", disponibilidad);
                    }
                }
            });
        }
    }
}
