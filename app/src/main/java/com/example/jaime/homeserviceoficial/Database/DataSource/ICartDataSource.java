package com.example.jaime.homeserviceoficial.Database.DataSource;

import com.example.jaime.homeserviceoficial.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource {

    Flowable<List<Cart>> getCartItem();
    Flowable<List<Cart>> getCartItemById(int carItemId);
    Flowable<List<Cart>> getCartItemByIdProducto(int idproducto);
    boolean getIdProducto(int idproducto);
    void updateProducto(int amount_p, int cantidad_p, double precio_p, double precio_total_p , int idproducto_p);
    int countCartItems();
    double sumPrecio();
    void  emptyCart();
    void insertToCart(Cart...carts);
    void upadateCart(Cart...carts);
    void deleteCartItem(Cart cart);
}
