package Negocio;

public class Horario {
    private String hora;
    private Boolean disponible;
    private Boolean seleccionado;

    public Boolean getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public Horario(String hora, Boolean disponible) {
        this.hora = hora;
        this.disponible = disponible;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
// Getters y setters aquí
}