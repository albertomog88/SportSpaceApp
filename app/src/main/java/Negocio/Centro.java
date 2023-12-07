package Negocio;

import java.util.ArrayList;
import Integracion.CentroDB;

public class Centro {
    private String  nombre;
    private double  latitud;
    private double  longitud;
    private double distancia;

    private ArrayList<String> idCampos;
    private CentroDB cDB;

    public Centro(){
        cDB = new CentroDB();
    }
    public Centro(String nombre, ArrayList<String> idCampos, double latitud, double longitud) {
        this.nombre = nombre;
        this.idCampos = idCampos;
        this.cDB = new CentroDB();
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public void obtenerCentros(double userLatitud, double userLongitud, CentroDB.CentroCallback callback){
        cDB.obtenerCentros(userLatitud, userLongitud, callback);
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<String> getIdCampos() {
        return idCampos;
    }

    public void setIdCampos(ArrayList<String> idCampos) {
        this.idCampos = idCampos;
    }
    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
}
