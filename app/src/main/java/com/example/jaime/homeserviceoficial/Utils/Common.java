package com.example.jaime.homeserviceoficial.Utils;

import com.example.jaime.homeserviceoficial.Database.DataSource.CartRepository;

import com.example.jaime.homeserviceoficial.Database.DataSource.FavoriteRepository;
import com.example.jaime.homeserviceoficial.Database.Local.JCMRoomCartDatabase;
import com.example.jaime.homeserviceoficial.Model.Categoria;
import com.example.jaime.homeserviceoficial.Model.Users;
import com.example.jaime.homeserviceoficial.Retrofit.ICarritoShopAPI;
import com.example.jaime.homeserviceoficial.Retrofit.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Common {

    //private static final String BASE_URL = "http://192.168.43.34:3000/api/";
    public static final String BASE_URL = "http://192.168.43.34:3000/";

    public static Users currentUser = null;

    public static Categoria currentCategory=null;

    //Base de datos
    public static JCMRoomCartDatabase jcmRoomCartDatabase;
    public static CartRepository cartRepository;
    public static FavoriteRepository favoriteRepository;

    public  static ICarritoShopAPI getAPI()
    {


        return RetrofitClient.getClient(BASE_URL).create(ICarritoShopAPI.class);
    }
}
