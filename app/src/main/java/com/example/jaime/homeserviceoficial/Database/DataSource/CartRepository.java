package com.example.jaime.homeserviceoficial.Database.DataSource;

import com.example.jaime.homeserviceoficial.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartRepository implements ICartDataSource{

    private ICartDataSource iCartDataSource;

    public CartRepository(ICartDataSource iCartDataSource) {
        this.iCartDataSource = iCartDataSource;
    }

    private static CartRepository instance;

    public  static CartRepository getInstance (ICartDataSource iCartDataSource)
    {
        if(instance == null)
            instance = new CartRepository(iCartDataSource);
        return  instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItem() {
        return iCartDataSource.getCartItem();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int carItemId) {
        return iCartDataSource.getCartItemById(carItemId);
    }

    @Override
    public  Flowable<List<Cart>> getCartItemByIdProducto(int idproducto) {
        return iCartDataSource.getCartItemByIdProducto(idproducto);
    }

    @Override
    public boolean getIdProducto( int idproducto) {
        return iCartDataSource.getIdProducto(idproducto);
    }

    @Override
    public void updateProducto(int amount_p, int cantidad_p, double precio_p, double precio_total_p , int idproducto_p) {
        iCartDataSource.updateProducto(  amount_p,  cantidad_p,  precio_p,  precio_total_p ,  idproducto_p);
    }

    @Override
    public int countCartItems() {
        return iCartDataSource.countCartItems();
    }

    @Override
    public double sumPrecio() {
        return iCartDataSource.sumPrecio();
    }

    @Override
    public double getPrecioTotal() {
        return iCartDataSource.getPrecioTotal();
    }

    @Override
    public void  emptyCart() {
        iCartDataSource.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        iCartDataSource.insertToCart(carts);
    }

    @Override
    public void upadateCart(Cart... carts) {
        iCartDataSource.upadateCart(carts);

    }

    @Override
    public void deleteCartItem(Cart cart) {
        iCartDataSource.deleteCartItem(cart);

    }
}
