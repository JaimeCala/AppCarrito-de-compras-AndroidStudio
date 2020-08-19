package com.example.jaime.homeserviceoficial.Database.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.jaime.homeserviceoficial.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CartDAO {

    @Query("SELECT * FROM Cart")
    Flowable<List<Cart>> getCartItem();

    @Query("SELECT * FROM cart WHERE id=:cartItemId")
    Flowable<List<Cart>> getCartItemById(int cartItemId);

    @Query("SELECT COUNT(*) FROM cart ")
    int countCartItems();

    @Query("SELECT SUM(precio) FROM cart ")
    float sumPrecio();

    @Query("DELETE FROM Cart")
    void emptyCart();

    @Insert
    void insertToCart(Cart...carts);
    @Update
    void upadateCart(Cart...carts);
    @Delete
    void deleteCartItem(Cart cart);
}
