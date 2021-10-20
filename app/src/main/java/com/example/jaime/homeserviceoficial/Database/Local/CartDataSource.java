package com.example.jaime.homeserviceoficial.Database.Local;

import com.example.jaime.homeserviceoficial.Database.DataSource.ICartDataSource;
import com.example.jaime.homeserviceoficial.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

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
    public Flowable<List<Cart>> getCartItemByIdProducto(int idproducto) {
        return cartDAO.getCartItemByIdProducto(idproducto);
    }

    @Override
    public boolean getIdProducto(int idproducto) {
        return cartDAO.getIdProducto(idproducto);
    }

    @Override
    public void updateProducto(int amount_p, int cantidad_p, double precio_p, double precio_total_p , int idproducto_p) {
        cartDAO.updateProducto(  amount_p,  cantidad_p,  precio_p,  precio_total_p ,  idproducto_p);
    }

    @Override
    public int countCartItems() {
        return cartDAO.countCartItems();
    }

    @Override
    public double sumPrecio() {
        return cartDAO.sumPrecio();
    }

    @Override
    public double getPrecioTotal() {
        return cartDAO.getPrecioTotal();
    }

    @Override
    public Completable< Integer> emptyCart(int idcart) {
      return   cartDAO.emptyCart(idcart);
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
