package app.micromarket.marza.homeserviceoficial.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Producto {

    @SerializedName("idproducto")
    @Expose
    private int idproducto;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    @SerializedName("precio")
    @Expose
    private Double precio;

    @SerializedName("peso")
    @Expose
    private Double peso;

    @SerializedName("idcategoria")
    @Expose
    private int idcategoria;

    @SerializedName("imgproductos")
    @Expose
    private List<ImgProductos> imgproductos;

    @SerializedName("unidadproductos")
    @Expose
    private List<UnidadProductos> unidadproductos;

    public Producto(int idproducto, String nombre, String descripcion, Double precio, Double peso, int idcategoria, List<ImgProductos> imgproductos, List<UnidadProductos> unidadproductos) {
        this.idproducto = idproducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.peso = peso;
        this.idcategoria = idcategoria;
        this.imgproductos = imgproductos;
        this.unidadproductos = unidadproductos;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public int getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(int idcategoria) {
        this.idcategoria = idcategoria;
    }

    public List<ImgProductos> getImgproductos() {
        return imgproductos;
    }

    public void setImgproductos(List<ImgProductos> imgproductos) {
        this.imgproductos = imgproductos;
    }

    public List<UnidadProductos> getUnidadproductos() {
        return unidadproductos;
    }

    public void setUnidadproductos(List<UnidadProductos> unidadproductos) {
        this.unidadproductos = unidadproductos;
    }


    //generamos una clase para las tablas relacionadas

    public static class ImgProductos{

        @SerializedName("idimgproducto")
        @Expose
        private int idimgproducto;

        @SerializedName("nombreimgprodu")
        @Expose
        private String nombreimgprodu;

        @SerializedName("linkimgprodu")
        @Expose
        private String linkimgprodu;

        @SerializedName("idproducto")
        @Expose
        private int idproducto;


        public int getIdimgproducto() {
            return idimgproducto;
        }

        public void setIdimgproducto(int idimgproducto) {
            this.idimgproducto = idimgproducto;
        }

        public String getNombreimgprodu() {
            return nombreimgprodu;
        }

        public void setNombreimgprodu(String nombreimgprodu) {
            this.nombreimgprodu = nombreimgprodu;
        }

        public String getLinkimgprodu() {
            return linkimgprodu;
        }

        public void setLinkimgprodu(String linkimgprodu) {
            this.linkimgprodu = linkimgprodu;
        }

        public int getIdproducto() {
            return idproducto;
        }

        public void setIdproducto(int idproducto) {
            this.idproducto = idproducto;
        }
    }

    public static  class UnidadProductos{

        @SerializedName("idunidadproducto")
        @Expose
        private int idunidadproducto;

        @SerializedName("valor")
        @Expose
        private String valor;

        @SerializedName("idproducto")
        @Expose
        private int idproducto;




        public int getIdunidadproducto() {
            return idunidadproducto;
        }

        public void setIdunidadproducto(int idunidadproducto) {
            this.idunidadproducto = idunidadproducto;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }

        public int getIdproducto() {
            return idproducto;
        }

        public void setIdproducto(int idproducto) {
            this.idproducto = idproducto;
        }
    }
}
