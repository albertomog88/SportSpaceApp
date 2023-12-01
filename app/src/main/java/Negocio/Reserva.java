package Negocio;

import Integracion.ReservaBD;

public class Reserva {
    private String fecha;
    private String hora;
    private String idCampo;
    private String centro;
    private String nombreCampo;
    private ReservaBD rBD;

    public Reserva(String fecha, String hora, String idCampo, String centro, String nombreCampo) {
        this.fecha = fecha;
        this.hora = hora;
        this.idCampo = idCampo;
        this.centro = centro;
        this.nombreCampo = nombreCampo;
        this.rBD = new ReservaBD();
    }

    public Reserva(){
        this.rBD = new ReservaBD();
    }

    public void obtenerReservas(String idUsuario, ReservaBD.Callback callback){
        rBD.obtenerReservas(idUsuario, callback);
    }

    public void eliminarReserva(String idUsuario, String idCampo, String fecha, String hora){
        rBD.eliminarReserva(idUsuario, idCampo, fecha, hora);
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(String idCampo) {
        this.idCampo = idCampo;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getNombreCampo() { return this.nombreCampo;}

    public void setNombreCampo(String nombreCampo){ this.nombreCampo = nombreCampo;   }

}
