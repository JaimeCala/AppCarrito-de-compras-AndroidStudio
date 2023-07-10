package app.micromarket.marza.homeserviceoficial.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cliente {



    private boolean success;

    @SerializedName("idcliente")
    @Expose
    private int idcliente;

    @SerializedName("idusuario")
    @Expose
    private int idusuario;

    public Cliente(int idcliente, int idusuario) {
        this.idcliente = idcliente;
        this.idusuario = idusuario;
    }

    public int getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(int idcliente) {
        this.idcliente = idcliente;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
