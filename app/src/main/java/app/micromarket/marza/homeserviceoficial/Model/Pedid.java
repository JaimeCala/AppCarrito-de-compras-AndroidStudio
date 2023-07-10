package app.micromarket.marza.homeserviceoficial.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Pedid {




    @SerializedName("latitud")
    @Expose
    private String latitud;

    @SerializedName("longitud")
    @Expose
    private String longitud;

    @SerializedName("fecha")
    @Expose
    private Date fecha;

    @SerializedName("hora")
    @Expose
    private Date hora;

    @SerializedName("estado")
    @Expose
    private String estado;

    @SerializedName("comentario")
    @Expose
    private String comentario;

    @SerializedName("idcliente")
    @Expose
    private int idcliente;


    @SerializedName("direccion")
    @Expose
    private String direccion;

    @SerializedName("precio")
    @Expose
    private double precio;

    @SerializedName("idpedido")
    @Expose
    private int idpedido;

    public Pedid() {
    }

    public Pedid(String latitud, String longitud, Date fecha, Date hora, String estado, String comentario, int idcliente, String direccion, double precio, int idpedido) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.comentario = comentario;
        this.idcliente = idcliente;
        this.direccion = direccion;
        this.precio = precio;
        this.idpedido = idpedido;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(int idcliente) {
        this.idcliente = idcliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(int idpedido) {
        this.idpedido = idpedido;
    }
}
