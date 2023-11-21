package Negocio;

import java.util.Map;

public class Campo {
    private int id;
    private String nombre;
    private String deporte;
    private Map<String, Map<String, Boolean>> disponibilidad;

    public Campo(int id, String nombre, String deporte, Map<String, Map<String, Boolean>> disponibilidad) {
        this.id = id;
        this.nombre = nombre;
        this.deporte = deporte;
        this.disponibilidad = disponibilidad;
    }



    public void setId(int id) {
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

    public int getId() {
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
