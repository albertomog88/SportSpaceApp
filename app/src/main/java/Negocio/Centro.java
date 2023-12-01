package Negocio;

import java.util.ArrayList;
import java.util.List;

import Integracion.CentroDB;
import Integracion.UsuarioDB;

public class Centro {
    private String  nombre;
    private String  localizacion;
    private double  latitud;
    private double  longitud;
    private double distancia;

    private ArrayList<String> idCampos;
    private CentroDB cDB;

    public Centro(){
        cDB = new CentroDB();
    }
    public Centro(String nombre, String localizacion, ArrayList<String> idCampos, double latitud, double longitud) {
        this.nombre = nombre;
        this.localizacion = localizacion;
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

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
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
