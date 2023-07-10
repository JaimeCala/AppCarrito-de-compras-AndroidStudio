package app.micromarket.marza.homeserviceoficial.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pedido {

    private boolean success;

    @SerializedName("idpedido")
    @Expose
    private int idpedido;


    @SerializedName("latitud")
    @Expose
    private String latitud;

    @SerializedName("longitud")
    @Expose
    private String longitud;

    /*@SerializedName("fecha")
    @Expose
    private Date fecha;

    @SerializedName("hora")
    @Expose
    private Date hora;


    @SerializedName("estado")
    @Expose
    private String estado;

    @SerializedName("idcliente")
    @Expose
    private int idcliente;

    @SerializedName("comentario")
    @Expose
    private String comentario;

    @SerializedName("direccion")
    @Expose
    private String direccion;

    @SerializedName("precio")
    @Expose
    private double precio;*/






    public Pedido(int idpedido, String latitud, String longitud
                  //, Date fecha, Date hora, String estado, int idcliente, String comentario, String direccion
                  //double precio

    ) {
        this.idpedido = idpedido;
        this.latitud = latitud;
        this.longitud = longitud;
        /*this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.idcliente = idcliente;
        this.comentario = comentario;
        this.direccion = direccion;*/
        //this.precio = precio;
    }

    public int getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(int idpedido) {
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

    /*public Date getFecha() {
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

    public int getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(int idcliente) {
        this.idcliente = idcliente;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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
    }*/

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}
