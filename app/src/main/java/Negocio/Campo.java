package Negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Integracion.CampoDB;

public class Campo {
    private String id;
    private String nombre;
    private String deporte;
    private Map<String, Map<String, Boolean>> disponibilidad;
    private CampoDB cDB;

    public Campo(String id, String nombre, String deporte, Map<String, Map<String, Boolean>> disponibilidad) {
        this.id = id;
        this.nombre = nombre;
        this.deporte = deporte;
        this.disponibilidad = disponibilidad;
        cDB = new CampoDB();
    }

    public Campo(String id, String nombre, String deporte) {
        this.id = id;
        this.nombre = nombre;
        this.deporte = deporte;
        cDB = new CampoDB();
    }

    public Campo(){
        cDB = new CampoDB();
    }

    public void obtenerCampos(ArrayList<String> idCampos, CampoDB.Callback callback){
        cDB.obtenerCampos(idCampos, callback);
    }

    public void verificarYActualizarDisponibilidad(String idBuscado, String fecha, CampoDB.Callback callback){
        cDB.verificarYActualizarDisponibilidad(idBuscado, fecha, callback);
    }

    public void insertarReserva(String idUsuario, String idCampo, String fecha, List<Horario> listaHoras){
        cDB.insertarReserva(idUsuario, idCampo, fecha, listaHoras);
    }

    public void actualizarDisponibilidad(String idCampo, String fecha, List<Horario> listaHoras){
        cDB.actualizarDisponibilidad(idCampo, fecha, listaHoras);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public void setDisponibilidad(Map<String, Map<String, Boolean>> disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDeporte() {
        return deporte;
    }

    public Map<String, Map<String, Boolean>> getDisponibilidad() {
        return disponibilidad;
    }
}
