package com.example.jaime.homeserviceoficial.Retrofit;

import com.example.jaime.homeserviceoficial.Database.ModelDB.Cart;
import com.example.jaime.homeserviceoficial.Model.Categoria;
import com.example.jaime.homeserviceoficial.Model.CategoriaProducto;
import com.example.jaime.homeserviceoficial.Model.Cliente;
import com.example.jaime.homeserviceoficial.Model.ImgCategoria;
import com.example.jaime.homeserviceoficial.Model.JWTToken;
import com.example.jaime.homeserviceoficial.Model.Login;
import com.example.jaime.homeserviceoficial.Model.Pedid;
import com.example.jaime.homeserviceoficial.Model.Pedido;
import com.example.jaime.homeserviceoficial.Model.PedidoProducto;
import com.example.jaime.homeserviceoficial.Model.Producto;
import com.example.jaime.homeserviceoficial.Model.Users;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ICarritoShopAPI {

    //crear usuario

    @FormUrlEncoded
    @POST("api/user/create")
    Call<Users> createUser (@Field("ci") String ci,
                            @Field("expedido") String expedido,
                            @Field("nombre") String nombre,
                            @Field("paterno") String paterno,
                            @Field("materno") String materno,
                            @Field("email") String email,
                            @Field("celular") String celular,
                            @Field("direccion") String direccion,
                            @Field("sexo") String sexo,
                            @Field("ciudad") String ciudad);
    //crear login

    @FormUrlEncoded
    @POST("api/login/create")
    Call<Login> createLogin(@Field("username") String username,
                            @Field("password") String password,
                            @Field("fecha") String fecha,
                            @Field("hora")String hora);

    //inicio de sesion


    @FormUrlEncoded
    @POST("api/auth/inicioSesion")
    Call<JWTToken> userLogin(@Field("username") String username, @Field("password") String password);

    //consulta un usuario para poner el nombre de quien esta iniciando sesion

    @FormUrlEncoded
    @POST("api/user/iconDrawer")
    Call<Users> getUserDrawerIcon(@Header("Authorization") String authorization, @Field("email") String email);

    //para traer menu categorias
    @GET("api/categoria/categorias")
    Observable<List<Categoria>> nombreCategoria();

    //traer lista imgcategoria

    @GET("api/img-categoria/imgcategorias")
    Observable<List<ImgCategoria>> imgCategoria();


    //Obtenemos lista de productos, relacionados con table unidadproductos y imgproductos
    @FormUrlEncoded
    @POST("api/categoria/idcateprodu")
    Observable<List<CategoriaProducto>> getProductosCate(@Field("idcategoria") int idcategoria);

    //endpoent para insertar to db cliente
    @FormUrlEncoded
    @POST("api/cliente/create")
    Call<Cliente> createCliente(@Field("user") int usuario);

    //insertamos pedidos

    @FormUrlEncoded
    @POST("api/pedido/create")
    Call<Pedido> createPedido(@Field("latitud") String latitud,
                             @Field("longitud") String longitud,
                             @Field("fecha") String fecha,
                             @Field("hora") String hora,
                             @Field("estado") String estado,
                             @Field("cliente") int cliente,
                             @Field("comentario") String comentario,
                             @Field("direccion") String direccion,
                             @Field("precio") double precio);

    //insertamos pedido y productos

    //@FormUrlEncoded
    //@Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("api/pedido-produ/create")
    Call<List<PedidoProducto>> createPedidoProducto(@Body()Cart[] cart);



}
