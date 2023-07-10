package app.micromarket.marza.homeserviceoficial.Utils;

import app.micromarket.marza.homeserviceoficial.Database.DataSource.CartRepository;

import app.micromarket.marza.homeserviceoficial.Database.DataSource.FavoriteRepository;
import app.micromarket.marza.homeserviceoficial.Database.Local.JCMRoomCartDatabase;
import app.micromarket.marza.homeserviceoficial.Model.Categoria;
import app.micromarket.marza.homeserviceoficial.Model.CategoriaProducto;
import app.micromarket.marza.homeserviceoficial.Model.Cliente;
import app.micromarket.marza.homeserviceoficial.Model.Pedido;
import app.micromarket.marza.homeserviceoficial.Model.PedidoProducto;
import app.micromarket.marza.homeserviceoficial.Model.Users;
import app.micromarket.marza.homeserviceoficial.Retrofit.ICarritoShopAPI;
import app.micromarket.marza.homeserviceoficial.Retrofit.RetrofitClient;

import java.util.List;

public class Common {

    //private static final String BASE_URL = "http://192.168.43.34:3000/";
    public static final String BASE_URL = "http://192.168.0.4:3000/";
    //public static final String BASE_URL_LOCAL = "http://10.0.2.2/";

    public static final String URL_NOTIFICACION_PEDIDO = "http://192.168.0.4:8181/";
    public static final String URL_NOTIFICACION_MENSAJE = "http://192.168.0.11:8182/";

    public static Users currentUser = null;

    public static Categoria currentCategory=null;

    public static Cliente currentCliente = null;

    public static CategoriaProducto currentCategoriaProducto = null;

    public static Pedido currentPedido = null;

    //probando para que regrese response correctamente
    //public static Pedid currentPedid = null;

    public static List<PedidoProducto> currentPedidoProducto = null;

    //Base de datos
    public static JCMRoomCartDatabase jcmRoomCartDatabase;
    public static CartRepository cartRepository;
    public static FavoriteRepository favoriteRepository;

    public  static ICarritoShopAPI getAPI()
    {


        return RetrofitClient.getClient(BASE_URL).create(ICarritoShopAPI.class);
    }
}
