package Negocio;

import java.util.ArrayList;
import Integracion.CentroDB;

public class Centro {
    private String  nombre;
    private String  localizacion;
    private ArrayList<String> idCampos;
    private CentroDB cDB;

    public Centro(){
        cDB = new CentroDB();
    }
    public Centro(String nombre, String localizacion, ArrayList<String> idCampos) {
        this.nombre = nombre;
        this.localizacion = localizacion;
        this.idCampos = idCampos;
        this.cDB = new CentroDB();
    }

    public ArrayList<Centro> obtenerCentros(String filtro_busqueda, String texto_busqueda, CentroDB.CentroCallback callback){
        return cDB.obtenerCentros(filtro_busqueda, texto_busqueda, callback);
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
}
