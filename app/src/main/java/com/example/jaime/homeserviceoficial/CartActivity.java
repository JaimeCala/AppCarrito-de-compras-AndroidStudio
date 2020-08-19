package com.example.jaime.homeserviceoficial;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.jaime.homeserviceoficial.Adapter.CartAdapter;
import com.example.jaime.homeserviceoficial.Database.ModelDB.Cart;
import com.example.jaime.homeserviceoficial.Retrofit.ICarritoShopAPI;
import com.example.jaime.homeserviceoficial.Utils.Common;
import com.example.jaime.homeserviceoficial.Utils.RecyclerItemTouchHelper;
import com.example.jaime.homeserviceoficial.Utils.RecyclerItemTouchHelperListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener{

    RecyclerView recycler_cart;
    Button btn_cart_pedido;
    public TextView txt_cart_preciototal;
    ElegantNumberButton txt_cartContador;
    final  int h=0;

    CompositeDisposable compositeDisposable;


    List<Cart> cartList = new ArrayList<>();

    CartAdapter cartAdapter;

    RelativeLayout rootLayout;


    ICarritoShopAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Productos agregados");
        compositeDisposable = new CompositeDisposable();

        mService = Common.getAPI();

        recycler_cart = findViewById(R.id.recycler_cart);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_cart);

        // mostrar precio total
        txt_cart_preciototal = findViewById(R.id.txt_preciototal_cart);
        txt_cart_preciototal.setText(new StringBuilder("Total: ").append(Common.cartRepository.sumPrecio()).append(" BS."));

        //mostrar automatico del precio total al manimular el incrementador

        //final double priceOne = cartList.get(h).precio/cartList.get(h).amount;

       /*txt_cartContador = findViewById(R.id.txt_amount_cart);
        txt_cartContador.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {


            }
        });*/

        //para realizar pedido

        btn_cart_pedido = findViewById(R.id.btn_cart_pedir);
       /* btn_cart_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colocarOrden();
            }
        });*/

        //INICIA LOS LAYOUT
        rootLayout = findViewById(R.id.rootLayout);

        //metodo para correr elementos de carrito
        loadCartElemntos();


    }

    private void loadCartElemntos() {

        compositeDisposable.add(Common.cartRepository.getCartItem()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) throws Exception {
                        displayCartElementos(carts);
                    }
                }));
    }

    private void displayCartElementos(List<Cart> carts) {
        cartList = carts;
        cartAdapter = new CartAdapter(this,carts);
        recycler_cart.setAdapter(cartAdapter);
    }

    //ctrl +o

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }




    @Override
    protected void onResume() {
        super.onResume();
        loadCartElemntos();

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof CartAdapter.CartViewHolder)
        {
            String nombre = cartList.get(viewHolder.getAdapterPosition()).nombre;
            final Cart deleteItem = cartList.get(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            //delete item from adapter
            cartAdapter.removeItem(deleteIndex);

            //delete item form Room database
            Common.cartRepository.deleteCartItem(deleteItem);
            Snackbar snackbar = Snackbar.make(rootLayout,new StringBuilder(nombre).append("Removido de la lista ").toString(),Snackbar.LENGTH_LONG);
            txt_cart_preciototal.setText(new StringBuilder("Total: ").append(Common.cartRepository.sumPrecio()).append(" BS."));
            snackbar.setAction("CANCELAR", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartAdapter.restoreItem(deleteItem,deleteIndex);
                    Common.cartRepository.insertToCart(deleteItem);
                    txt_cart_preciototal.setText(new StringBuilder("Total: ").append(Common.cartRepository.sumPrecio()).append(" BS."));

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
