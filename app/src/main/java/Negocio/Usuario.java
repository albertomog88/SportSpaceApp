package Negocio;

import java.lang.reflect.Constructor;

import Integracion.UsuarioDB;

public class Usuario {
    private String nombre;
    private String apellidos;
    private String email;
    private String pass;
    private String fecha;
    private UsuarioDB uDB;
    public Usuario (String nombre, String apellidos, String email, String pass, String fecha){
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.pass = pass;
        this.fecha = fecha;
        uDB = new UsuarioDB();
    }

    public boolean guardar(){
        return uDB.guardar(this);
    }
    public boolean existe(){return uDB.existe(this);}

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public String getFecha() {
        return fecha;
    }
}
