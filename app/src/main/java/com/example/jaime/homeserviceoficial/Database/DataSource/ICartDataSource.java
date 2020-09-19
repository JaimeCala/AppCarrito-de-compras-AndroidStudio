package com.example.jaime.homeserviceoficial.Database.DataSource;

import com.example.jaime.homeserviceoficial.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource {

    Flowable<List<Cart>> getCartItem();
    Flowable<List<Cart>> getCartItemById(int carItemId);
    int countCartItems();
    double sumPrecio();
    void  emptyCart();
    void insertToCart(Cart...carts);
    void upadateCart(Cart...carts);
    void deleteCartItem(Cart cart);
}
