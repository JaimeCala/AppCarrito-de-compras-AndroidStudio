package app.micromarket.marza.homeserviceoficial.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;




public class Categoria {

    @SerializedName("idcategoria")
    @Expose
    private int idcategoria;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("createdAt")
    @Expose
    private Date createdAt;

    @SerializedName("updatedAt")
    @Expose
    private Date updatedAt;

    @SerializedName("imgcategorias")
    @Expose
    private List<ImgCategoria> imgcategorias  ;

    @SerializedName("message")
    @Expose
    private String message;

    public Categoria(int idcategoria, String nombre, Date createdAt, Date updatedAt, List<ImgCategoria> imgcategorias, String message) {
        this.idcategoria = idcategoria;
        this.nombre = nombre;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.imgcategorias = imgcategorias;
        this.message = message;
    }

    public int getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(int idcategoria) {
        this.idcategoria = idcategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ImgCategoria> getImgcategorias() {
        return imgcategorias;
    }

    public void setImgcategorias(List<ImgCategoria> imgcategorias) {
        this.imgcategorias = imgcategorias;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //definir otra clase para imgcategoria

    public static class ImgCategoria{

        @SerializedName("idimgcategoria")
        @Expose
        private int idimgcategoria;

        @SerializedName("nombreimgcategoria")
        @Expose
        private String nombreimgcategoria;

        @SerializedName("linkimgcategoria")
        @Expose
        private String linkimgcategoria;

        @SerializedName("createdAt")
        @Expose
        private Date created_At;

        @SerializedName("updatedAt")
        @Expose
        private Date updated_At;

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

        public Date getCreated_At() {
            return created_At;
        }

        public void setCreated_At(Date created_At) {
            this.created_At = created_At;
        }

        public Date getUpdated_At() {
            return updated_At;
        }

        public void setUpdated_At(Date updated_At) {
            this.updated_At = updated_At;
        }
    }






}


