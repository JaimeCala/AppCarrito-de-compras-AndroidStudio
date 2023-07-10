package app.micromarket.marza.homeserviceoficial.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import app.micromarket.marza.homeserviceoficial.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CartDAO {

    @Query("SELECT * FROM Cart")
    Flowable<List<Cart>> getCartItem();

    @Query("SELECT * FROM cart WHERE id=:cartItemId")
    Flowable<List<Cart>> getCartItemById(int cartItemId);

    @Query("SELECT * FROM cart  WHERE producto=:idproducto")
    Flowable<List<Cart>> getCartItemByIdProducto(int idproducto);

    @Query("SELECT * FROM cart  WHERE producto=:idproducto")
    boolean getIdProducto(int idproducto);
    //



    @Query("UPDATE cart SET amount =:amount_p+amount,cantidad =:cantidad_p+cantidad, precio=:precio_p+precio, precio_total =:precio_total_p+precio_total, oferta =:oferta, porcentaje_des =:porcentaje_des WHERE producto=:idproducto_p")
    void updateProducto(int amount_p, int cantidad_p, double precio_p, double precio_total_p , String oferta, double porcentaje_des, int idproducto_p);

    @Query("SELECT COUNT(*) FROM cart ")
    int countCartItems();

    @Query("SELECT SUM(precio) FROM cart ")
    double sumPrecio();

    @Query("SELECT precio_total FROM cart")
    double getPrecioTotal();

    @Query("DELETE FROM Cart WHERE id=:idcart")
     void  emptyCart(int idcart);

    @Insert
    void insertToCart(Cart...carts);
    @Update
    void upadateCart(Cart...carts);
    @Delete
    void deleteCartItem(Cart cart);
}
