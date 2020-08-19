package com.example.jaime.homeserviceoficial.Database.Local;

import com.example.jaime.homeserviceoficial.Database.DataSource.ICartDataSource;
import com.example.jaime.homeserviceoficial.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartDataSource implements ICartDataSource {
    private CartDAO cartDAO;
    private static CartDataSource instance;

    public CartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    public static CartDataSource getInstance(CartDAO cartDAO)
    {
        if(instance == null)
            instance = new CartDataSource(cartDAO);
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItem() {
        return cartDAO.getCartItem();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int carItemId) {
        return cartDAO.getCartItemById(carItemId);
    }

    @Override
    public int countCartItems() {
        return cartDAO.countCartItems();
    }

    @Override
    public float sumPrecio() {
        return cartDAO.sumPrecio();
    }

    @Override
    public void emptyCart() {
        cartDAO.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        cartDAO.insertToCart(carts);

    }

    @Override
    public void upadateCart(Cart... carts) {
        cartDAO.upadateCart(carts);

    }

    @Override
    public void deleteCartItem(Cart cart) {
        cartDAO.deleteCartItem(cart);

    }
}
