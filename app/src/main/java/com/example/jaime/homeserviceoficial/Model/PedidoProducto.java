package com.example.jaime.homeserviceoficial.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PedidoProducto {
    @SerializedName("idcliente")
    @Expose
    private int idpedidoproducto;

    @SerializedName("cantidad")
    @Expose
    private int cantidad;

    @SerializedName("idpedido")
    @Expose
    private int idpedido;

    @SerializedName("idproducto")
    @Expose
    private int idproducto;

    public PedidoProducto(int idpedidoproducto, int cantidad, int idpedido, int idproducto) {
        this.idpedidoproducto = idpedidoproducto;
        this.cantidad = cantidad;
        this.idpedido = idpedido;
        this.idproducto = idproducto;
    }

    public int getIdpedidoproducto() {
        return idpedidoproducto;
    }

    public void setIdpedidoproducto(int idpedidoproducto) {
        this.idpedidoproducto = idpedidoproducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(int idpedido) {
        this.idpedido = idpedido;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }
}
