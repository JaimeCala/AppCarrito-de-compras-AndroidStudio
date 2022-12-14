package com.example.jaime.homeserviceoficial.Model;

public class Reclamo {

    private int idreclamo;
    private String comentario;
    private int idusuario;
    private String estado;

    public Reclamo(int idreclamo, String comentario, int idusuario, String estado) {
        this.idreclamo = idreclamo;
        this.comentario = comentario;
        this.idusuario = idusuario;
        this.estado = estado;
    }

    public int getIdreclamo() {
        return idreclamo;
    }

    public void setIdreclamo(int idreclamo) {
        this.idreclamo = idreclamo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
