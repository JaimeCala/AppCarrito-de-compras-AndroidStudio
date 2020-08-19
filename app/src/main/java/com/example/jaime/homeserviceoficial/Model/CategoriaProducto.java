package com.example.jaime.homeserviceoficial.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoriaProducto {

    @SerializedName("categoria_idcategoria")
    @Expose
    private int categoria_idcategoria;

    @SerializedName("categoria_nombre")
    @Expose
    private String categoria_nombre;

    @SerializedName("idprodu")
    @Expose
    private int idprodu;

    @SerializedName("produnombre")
    @Expose
    private String produnombre;

    @SerializedName("produdescripcion")
    @Expose
    private String produdescripcion;

    @SerializedName("produprecio")
    @Expose
    private Double produprecio;

    @SerializedName("produpeso")
    @Expose
    private Double produpeso;

    @SerializedName("imgidprodu")
    @Expose
    private int imgidprodu;

    @SerializedName("imgnombreprodu")
    @Expose
    private String imgnombreprodu;

    @SerializedName("iduniprodu")
    @Expose
    private int iduniprodu;

    @SerializedName("univalor")
    @Expose
    private String univalor;

    public CategoriaProducto(int categoria_idcategoria, String categoria_nombre, int idprodu, String produnombre,String produdescripcion, Double produprecio, Double produpeso, int imgidprodu, String imgnombreprodu, int iduniprodu, String univalor) {
        this.categoria_idcategoria = categoria_idcategoria;
        this.categoria_nombre = categoria_nombre;
        this.idprodu = idprodu;
        this.produnombre = produnombre;
        this.produdescripcion=produdescripcion;
        this.produprecio = produprecio;
        this.produpeso = produpeso;
        this.imgidprodu = imgidprodu;
        this.imgnombreprodu = imgnombreprodu;
        this.iduniprodu = iduniprodu;
        this.univalor = univalor;
    }

    public int getCategoria_idcategoria() {
        return categoria_idcategoria;
    }

    public void setCategoria_idcategoria(int categoria_idcategoria) {
        this.categoria_idcategoria = categoria_idcategoria;
    }

    public String getCategoria_nombre() {
        return categoria_nombre;
    }

    public void setCategoria_nombre(String categoria_nombre) {
        this.categoria_nombre = categoria_nombre;
    }

    public int getIdprodu() {
        return idprodu;
    }

    public void setIdprodu(int idprodu) {
        this.idprodu = idprodu;
    }

    public String getProdunombre() {
        return produnombre;
    }

    public void setProdunombre(String produnombre) {
        this.produnombre = produnombre;
    }

    public String getProdudescripcion() {
        return produdescripcion;
    }

    public void setProdudescripcion(String produdescripcion) {
        this.produdescripcion = produdescripcion;
    }

    public Double getProduprecio() {
        return produprecio;
    }

    public void setProduprecio(Double produprecio) {
        this.produprecio = produprecio;
    }

    public Double getProdupeso() {
        return produpeso;
    }

    public void setProdupeso(Double produpeso) {
        this.produpeso = produpeso;
    }

    public int getImgidprodu() {
        return imgidprodu;
    }

    public void setImgidprodu(int imgidprodu) {
        this.imgidprodu = imgidprodu;
    }

    public String getImgnombreprodu() {
        return imgnombreprodu;
    }

    public void setImgnombreprodu(String imgnombreprodu) {
        this.imgnombreprodu = imgnombreprodu;
    }

    public int getIduniprodu() {
        return iduniprodu;
    }

    public void setIduniprodu(int iduniprodu) {
        this.iduniprodu = iduniprodu;
    }

    public String getUnivalor() {
        return univalor;
    }

    public void setUnivalor(String univalor) {
        this.univalor = univalor;
    }
}
