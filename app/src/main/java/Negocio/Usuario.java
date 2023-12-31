package Negocio;

import Integracion.UsuarioDB;

public class Usuario {
    private String nombre;
    private String apellidos;
    private String email;
    private String pass;
    private String fechaNacimiento;
    private static UsuarioDB uDB;

    public Usuario (){
        this.uDB = new UsuarioDB();
    }
    public Usuario (String nombre, String apellidos, String email, String pass, String fecha){
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.pass = pass;
        this.fechaNacimiento = fecha;
        uDB = new UsuarioDB();
    }

    public void getUsuario(String mail, UsuarioDB.Callback callback){
        uDB.getUsuario(mail, callback);
    }

    public boolean guardar(){
        return uDB.guardar(this);
    }
    public void existe(UsuarioDB.Callback callback){
        uDB.existe(this, callback);
    }

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
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String toString() {
        return this.email+" "+this.nombre+" "+this.apellidos+" "+this.fechaNacimiento+" "+this.pass;
    }
}
