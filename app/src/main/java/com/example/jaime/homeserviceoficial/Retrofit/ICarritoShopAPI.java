package com.example.jaime.homeserviceoficial.Retrofit;

import com.example.jaime.homeserviceoficial.Database.ModelDB.Cart;
import com.example.jaime.homeserviceoficial.Model.Banner;
import com.example.jaime.homeserviceoficial.Model.Categoria;
import com.example.jaime.homeserviceoficial.Model.CategoriaProducto;
import com.example.jaime.homeserviceoficial.Model.Cliente;
import com.example.jaime.homeserviceoficial.Model.Historial;
import com.example.jaime.homeserviceoficial.Model.ImgCategoria;
import com.example.jaime.homeserviceoficial.Model.JWTToken;
import com.example.jaime.homeserviceoficial.Model.Login;
import com.example.jaime.homeserviceoficial.Model.Pedid;
import com.example.jaime.homeserviceoficial.Model.Pedido;
import com.example.jaime.homeserviceoficial.Model.PedidoProducto;
import com.example.jaime.homeserviceoficial.Model.Producto;
import com.example.jaime.homeserviceoficial.Model.Reclamo;
import com.example.jaime.homeserviceoficial.Model.Users;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ICarritoShopAPI {

    //crear usuario

    @FormUrlEncoded
    @POST("api/user/create")
    Observable<Users> createUser (@Field("ci") String ci,
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
    Observable<Login> createLogin(
                            @Field("user") int idusuario,
                            @Field("username") String username,
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

    //historial de pedidos
    @GET("api/pedido/pedidorealizado/{idusuario}")
    Call<List<Historial>> historialPedido(@Path("idusuario") int idusuario);


    //Obtenemos lista de productos, relacionados con table unidadproductos y imgproductos
    @FormUrlEncoded
    @POST("api/categoria/idcateprodu")
    Observable<List<CategoriaProducto>> getProductosCate(@Field("idcategoria") int idcategoria);

    //obtenemos productos en oferta
    @GET("api/producto/productosOferta")
    Call<List<CategoriaProducto>> getProductosOferta();

    //obtenemos banners
    @GET("api/banner/banners")
    Observable<List<Banner>> getBanner();


    //endpoent para insertar to db cliente
    @FormUrlEncoded
    @POST("api/cliente/create")
    Observable<Cliente> createCliente(@Field("user") int usuario);

    //insertamos pedidos

    /*@FormUrlEncoded
    @POST("api/pedido/create")
    Observable<Pedido> createPedido(@Field("latitud") String latitud,
                             @Field("longitud") String longitud,
                             @Field("fecha") String fecha,
                             @Field("hora") String hora,
                             //@Field("estado") String estado,
                             @Field("cliente") int cliente,
                             @Field("comentario") String comentario,
                             @Field("direccion") String direccion,
                             @Field("precio") double precio);*/

    //insertamos pedido y productos

    //-----subida pedido pdf
    @Multipart
    @POST("api/pedido/create")
    Observable<Pedido> createPedido(@Part("latitud") String latitud,
                                    @Part("longitud") String longitud,
                                    @Part("fecha") String fecha,
                                    @Part("hora") String hora,
                                    //@Field("estado") String estado,
                                    @Part("cliente") int cliente,
                                    @Part("comentario") String comentario,
                                    @Part("direccion") String direccion,
                                    @Part("precio") double precio,
                                    @Part  MultipartBody.Part file

                                    );
    //----fin pedido pdf
    //updateUser(@Part("photo") RequestBody photo, @Part("description") RequestBody description);

    //@Headers({"Content-Type: application/x-www-form-urlencoded"})
    //@FormUrlEncoded
    @POST("api/pedido-produ/create/{pedido}")
    Observable<List<PedidoProducto>> createPedidoProducto( @Path("pedido") int pedido, @Body Cart[] cart);
    //Observable<List<PedidoProducto>> createPedidoProducto(@Field("pedido") int idpedido ,@Field("cart") Cart[] cart);
    //Observable<List<PedidoProducto>> createPedidoProducto(@Query("pedido") int idpedido ,@Body()Cart[] cart);


    @FormUrlEncoded
    @POST("api/reclamo/create")
    Call<Reclamo> comentarioReclamo(@Field("comentario") String comentario, @Field("user") int user);


    //subir pdf




}
