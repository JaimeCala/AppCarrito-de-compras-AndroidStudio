package com.example.jaime.homeserviceoficial.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.jaime.homeserviceoficial.CartActivity;
import com.example.jaime.homeserviceoficial.Database.ModelDB.Cart;
import com.example.jaime.homeserviceoficial.R;
import com.example.jaime.homeserviceoficial.Utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    //Context context;
    CartActivity cartActivity;
    List<Cart> cartList;

    public CartAdapter(CartActivity cartActivity, List<Cart> cartList) {
        this.cartActivity = cartActivity;
        this.cartList = cartList;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(cartActivity).inflate(R.layout.cart_item_layout,parent,false);

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CartViewHolder holder, final int position) {
        Picasso.with(cartActivity)
                .load(cartList.get(position).imgnombre)
                .into(holder.img_producto);

        holder.txt_cart_amount.setNumber(String.valueOf(cartList.get(position).amount));
        holder.txt_cart_precioUnitario.setText(new StringBuilder("BS.").append(cartList.get(position).precio));
        holder.txt_cart_producto_nombre.setText(cartList.get(position).nombre);
        //holder.txt_cart_precioSumado.setText(String.valueOf(Common.cartRepository.sumPrecio()));
        //holder.txt_cart_precioSumado.setText(new StringBuilder("Total: ").append(Common.cartRepository.sumPrecio()).append(" BS."));


        //traer precio de un producto


        final double priceOne = cartList.get(position).precio/cartList.get(position).amount;

        //auto guardado de los elementos cuando el usuario cambia de contador
        holder.txt_cart_amount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart = cartList.get(position);
                cart.amount = newValue;
                cart.precio = Math.round(priceOne*newValue);
                Common.cartRepository.upadateCart(cart);
                Common.cartRepository.sumPrecio();



                Toast.makeText(cartActivity, "Precio Total :"+Common.cartRepository.sumPrecio(), Toast.LENGTH_SHORT).show();

                holder.txt_cart_precioUnitario.setText(new StringBuilder("BS.").append(cartList.get(position).precio));



                cartActivity.txt_cart_preciototal.setText(new StringBuilder("Total: ").append(Common.cartRepository.sumPrecio()).append(" BS."));

                // txt_sumas.setText(new StringBuilder("ll").append(Common.cartRepository.sumPrecio()));


                //holder.txt_cart_precioSumado.setText(new StringBuilder("Total: ").append(Common.cartRepository.sumPrecio()).append(" BS."));


            }
        });




    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        ImageView img_producto;
        TextView txt_cart_producto_nombre,txt_cart_precioUnitario;
        public TextView txt_cart_precioSumado;
        ElegantNumberButton txt_cart_amount;

        //para eleminar
        public RelativeLayout view_background;
        public LinearLayout view_foreground;

        public CartViewHolder(View itemView) {
            super(itemView);

            img_producto = itemView.findViewById(R.id.img_product_cart);
            txt_cart_amount = itemView.findViewById(R.id.txt_amount_cart);
            txt_cart_producto_nombre = itemView.findViewById(R.id.txt_cart_product_nombre);
            txt_cart_precioUnitario = itemView.findViewById(R.id.txt_cart_product_precioUnidad);

            view_background = itemView.findViewById(R.id.view_background);
            view_foreground = itemView.findViewById(R.id.view_foreground);


            txt_cart_precioSumado = itemView.findViewById(R.id.txt_preciototal_cart);
        }
    }

    public  void removeItem(int position)
    {
        cartList.remove(position);
        notifyItemRemoved(position);

    }
    public void restoreItem(Cart item, int position)
    {
        cartList.add(position,item);
        notifyItemInserted(position);
    }
}
