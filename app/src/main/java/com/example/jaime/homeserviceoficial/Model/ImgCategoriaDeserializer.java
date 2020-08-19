package com.example.jaime.homeserviceoficial.Model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONArray;

import java.io.Console;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImgCategoriaDeserializer implements JsonDeserializer<ArrayList<ImgCategoria>> {



    @Override
    public ArrayList<ImgCategoria> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList imgcategorias = new ArrayList<>();

        final JsonObject jsonObject = json.getAsJsonObject();
        //final JsonObject jsonObject = jsonArray.getAsJsonObject();
       /* final int idcategoria = jsonObject.get("idcategoria").getAsInt();
        final String nombre = jsonObject.get("nombre").getAsString();*/
        //final JsonArray itemJson = json.getAsJsonArray();
        final JsonArray itemJsonArray = jsonObject.get("imgcategorias").getAsJsonArray();
        //final  JsonArray itemArray = categoriaList.toArray();





        for(JsonElement itemsJsonElement: itemJsonArray){

            final JsonObject itemJsonObject = itemsJsonElement.getAsJsonObject();
            final int idimgcategoria =itemJsonObject.get("idimgcategoria").getAsInt();
            final String nombreimgcategoria = itemJsonObject.get("nombreimgcategoria").getAsString();
            final String linkimgcategoria = itemJsonObject.get("linkimgcategoria").getAsString();
            //final int idcategoria = itemJsonObject.get("idcategoria").getAsInt();
            //final String message = itemJsonObject.get("message").getAsString();

            imgcategorias.add(new ImgCategoria(idimgcategoria, nombreimgcategoria,linkimgcategoria));

        }
        return imgcategorias;



    }


   /* @Override
    public ArrayList<Categoria> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {


            JsonElement imgcategorias = json.getAsJsonObject().get("imgcategorias");

            return new Gson().fromJson(imgcategorias,Categoria.class);
            return  new Gson().fromJson(imgcategorias,Arr);
    }*/
}
