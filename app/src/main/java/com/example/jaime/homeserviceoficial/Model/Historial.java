package com.example.jaime.homeserviceoficial.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Historial {

    @SerializedName("idpedido")
    @Expose
    private int idpedido;

    @SerializedName("comentario")
    @Expose
    private String comentario;

    @SerializedName("precio")
    @Expose
    private float precio;

    @SerializedName("fecha")
    @Expose
    private String fecha;

    @SerializedName("hora")
    @Expose
    private String hora;

    @SerializedName("estado")
    @Expose
    private String estado;


    public Historial(int idpedido, String comentario, float precio, String fecha, String hora, String estado) {
        this.idpedido = idpedido;
        this.comentario = comentario;
        this.precio = precio;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public int getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(int idpedido) {
        this.idpedido = idpedido;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
