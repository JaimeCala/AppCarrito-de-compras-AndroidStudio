package app.micromarket.marza.homeserviceoficial.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PedidoProducto {



    @SerializedName("idpedidoproducto")
    @Expose
    private int idpedidoproducto;

    @SerializedName("cantidad")
    @Expose
    private int cantidad;

    @SerializedName("precio_uni")
    @Expose
    private Double precio_uni;

    @SerializedName("precio_total")
    @Expose
    private Double precio_total;



    @SerializedName("idpedido")
    @Expose
    private int idpedido;

    @SerializedName("idproducto")
    @Expose
    private int idproducto;

    public PedidoProducto(int idpedidoproducto, int cantidad,Double precio_uni, Double precio_total ,int idpedido, int idproducto) {
        this.idpedidoproducto = idpedidoproducto;
        this.cantidad = cantidad;
        this.precio_uni = precio_uni;
        this.precio_total = precio_total;
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

    public Double getPrecio_uni() {
        return precio_uni;
    }

    public void setPrecio_uni(Double precio_uni) {
        this.precio_uni = precio_uni;
    }

    public Double getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(Double precio_total) {
        this.precio_total = precio_total;
    }


}
