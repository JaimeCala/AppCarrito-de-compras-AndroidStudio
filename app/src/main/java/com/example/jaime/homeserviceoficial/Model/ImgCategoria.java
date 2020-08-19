package com.example.jaime.homeserviceoficial.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ImgCategoria {

    @SerializedName("idimgcategoria")
    @Expose
    private  int idimgcategoria;
    @SerializedName("nombreimgcategoria")
    @Expose
    public String nombreimgcategoria;
    @SerializedName("linkimgcategoria")
    @Expose
    private String linkimgcategoria;
    /*@SerializedName("created_at")
    @Expose
    private Date created_at;
    @SerializedName("updated_at")
    @Expose
    private Date updated_at;
    @SerializedName("idcategoria")
    @Expose
    private int idcategoria;*/
   /* @SerializedName("message")
    @Expose
    private String message;*/

    public ImgCategoria(int idimgcategoria, String nombreimgcategoria, String linkimgcategoria) {
        this.idimgcategoria = idimgcategoria;
        this.nombreimgcategoria = nombreimgcategoria;
        this.linkimgcategoria = linkimgcategoria;
       // this.created_at = created_at;
       // this.updated_at = updated_at;
       // this.idcategoria = idcategoria;
        //this.message = message;
    }

    public int getIdimgcategoria() {
        return idimgcategoria;
    }

    public void setIdimgcategoria(int idimgcategoria) {
        this.idimgcategoria = idimgcategoria;
    }

    public String getNombreimgcategoria() {
        return nombreimgcategoria;
    }

    public void setNombreimgcategoria(String nombreimgcategoria) {
        this.nombreimgcategoria = nombreimgcategoria;
    }

    public String getLinkimgcategoria() {
        return linkimgcategoria;
    }

    public void setLinkimgcategoria(String linkimgcategoria) {
        this.linkimgcategoria = linkimgcategoria;
    }

    /*public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public int getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(int idcategoria) {
        this.idcategoria = idcategoria;
    }*/

    /*public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }*/
}
