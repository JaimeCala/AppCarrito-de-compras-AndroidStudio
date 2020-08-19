package com.example.jaime.homeserviceoficial.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JWTToken {

    @SerializedName("token")
    @Expose

    public String token;

    public JWTToken() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
