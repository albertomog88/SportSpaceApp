package Negocio;

import java.util.ArrayList;
import java.util.List;

import Integracion.CentroDB;
import Integracion.UsuarioDB;

public class Centro {
    private String  nombre;
    private String  localizacion;
    private ArrayList<Integer> idCampos;
    private CentroDB cDB;

    public Centro(){
        cDB = new CentroDB();
    }
    public Centro(String nombre, String localizacion, ArrayList<Integer> idCampos) {
        this.nombre = nombre;
        this.localizacion = localizacion;
        this.idCampos = idCampos;
        this.cDB = new CentroDB();
    }

    public ArrayList<Centro> obtenerCentros(CentroDB.CentroCallback callback){
        return cDB.obtenerCentros(callback);
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

    public ArrayList<Integer> getIdCampos() {
        return idCampos;
    }

    public void setIdCampos(ArrayList<Integer> idCampos) {
        this.idCampos = idCampos;
    }
}
