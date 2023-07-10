package app.micromarket.marza.homeserviceoficial.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import app.micromarket.marza.homeserviceoficial.Database.ModelDB.Cart;
import app.micromarket.marza.homeserviceoficial.Database.ModelDB.Favorite;
import app.micromarket.marza.homeserviceoficial.Interface.ItemClickListener;
import app.micromarket.marza.homeserviceoficial.Model.CategoriaProducto;
import app.micromarket.marza.homeserviceoficial.R;
import app.micromarket.marza.homeserviceoficial.Utils.Common;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriaProductoAdapter extends RecyclerView.Adapter<CategoriaProductoViewHolder> {


    Context context;
    List<CategoriaProducto> categoriaProductoList;

    List<Cart> cartList;

    //NotificationBadge badge;
    //
    //final double precioenvio=0;

    public int elegantcounter;


    String url= "api/img-producto/";

    public CategoriaProductoAdapter(List<Cart> cartList) {
        this.cartList = cartList;
    }

    public CategoriaProductoAdapter(Context context, List<CategoriaProducto> categoriaProductoList) {
        this.context = context;
        this.categoriaProductoList = categoriaProductoList;
    }

    @Override
    public CategoriaProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.producto_item_layout,null);
        return new CategoriaProductoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoriaProductoViewHolder holder, final int position) {





        if( categoriaProductoList.get(holder.getAdapterPosition()).getProduoferta() != "NO"){
            holder.txt_precioOferta.setText(new StringBuilder("BS.").append(categoriaProductoList.get(holder.getAdapterPosition()).getProduporcentaje()).toString());


        }
            holder.txt_precioProducto.setText(new StringBuilder("BS.").append(categoriaProductoList.get(holder.getAdapterPosition()).getProduprecio()).toString());

            //holder.txt_precioOferta.setText(new StringBuilder("BS.").append(categoriaProductoList.get(holder.getAdapterPosition()).getProduoferta()));

            holder.txt_nombreProducto.setText(categoriaProductoList.get(holder.getAdapterPosition()).getProdunombre());
            holder.txt_pesoProducto.setText(String.format(categoriaProductoList.get(holder.getAdapterPosition()).getProdupeso().toString()));
            holder.txt_unidadProducto.setText(categoriaProductoList.get(holder.getAdapterPosition()).getUnivalor());

        //evento click boton agregar producto
        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAddToCartDialog(holder.getAdapterPosition());
            }
        });
        //mostramos imagen de productos
        Picasso.with(context)
                .load(Common.BASE_URL+url+categoriaProductoList.get(holder.getAdapterPosition()).getImgnombreprodu())
                .into(holder.img_producto);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //Favoritos sys

        if (Common.favoriteRepository.isFavorite(categoriaProductoList.get(position).getIdprodu())== 1)
            holder.btn_favorito.setImageResource(R.drawable.ic_favorite_black_24dp);
        else
            holder.btn_favorito.setImageResource(R.drawable.ic_favorite_border_white_24dp);

        holder.btn_favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.favoriteRepository.isFavorite(categoriaProductoList.get(holder.getAdapterPosition()).getIdprodu())!=1)
                {
                    adicionarOrRemoveToFavorite(categoriaProductoList.get(holder.getAdapterPosition()),true);
                    holder.btn_favorito.setImageResource(R.drawable.ic_favorite_black_24dp);

                }
                else
                {
                    adicionarOrRemoveToFavorite(categoriaProductoList.get(holder.getAdapterPosition()),false);
                    holder.btn_favorito.setImageResource(R.drawable.ic_favorite_border_white_24dp);

                }
            }
        });



    }

    private void adicionarOrRemoveToFavorite(CategoriaProducto categoriaProducto, boolean isAdd) {

        Favorite favorite = new Favorite();
        favorite.id = categoriaProducto.getIdprodu();
        favorite.imgnombre=Common.BASE_URL+url+categoriaProducto.getImgnombreprodu();
        favorite.nombre= categoriaProducto.getProdunombre();
        favorite.precio= categoriaProducto.getProduprecio();
        favorite.categoriaId=categoriaProducto.getCategoria_idcategoria();

        if (isAdd)
        {
            Common.favoriteRepository.insertFav(favorite);
        }
        else
            Common.favoriteRepository.delete(favorite);

    }

    private void mostrarAddToCartDialog(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.add_carrito_layout,null);

        //vista o view

        ImageView img_producto_dialog = view.findViewById(R.id.img_producto_cart_id);
        final ElegantNumberButton txt_contador = view.findViewById(R.id.txt_count);
        final TextView txt_nombre_producto = view.findViewById(R.id.txt_dialogproducto_nombre);
        final TextView txt_precio_dialog = view.findViewById(R.id.txt_precio_dialog);
        final TextView txt_oferta_dialog = view.findViewById(R.id.txt_precio_oferta_dialog);
        final TextView txt_precio_sum = view.findViewById(R.id.txt_precio_suma_dialog);



        //set datos
        Picasso.with(context)
                .load(Common.BASE_URL+url+categoriaProductoList.get(position).getImgnombreprodu())
                .into(img_producto_dialog);
        txt_nombre_producto.setText(categoriaProductoList.get(position).getProdunombre());
        txt_precio_dialog.setText(new StringBuilder("Bs.").append(categoriaProductoList.get(position).getProduprecio()).toString());

        txt_oferta_dialog.setText(new StringBuilder("Bs.").append(categoriaProductoList.get(position).getProduporcentaje()).toString());

        txt_precio_sum.setText(new StringBuilder("Total: ").append(categoriaProductoList.get(position).getProduprecio()).append("Bs."));

        txt_precio_sum.setText(new StringBuilder("Total: ").append((categoriaProductoList.get(position).getProduprecio()-categoriaProductoList.get(position).getProduporcentaje())).append("Bs."));

        //creamos var para capturar el resultado de count y preci
         //final double precioenvio;

        //-------------------------------escuchando contador al cambio-------//
        txt_contador.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                if(categoriaProductoList.get(position).getProdustock()<=newValue){
                    elegantcounter = categoriaProductoList.get(position).getProdustock();

                   txt_contador.setRange(1,elegantcounter);
                   // txt_contador.setEnabled(false);
                }

                txt_precio_sum.setText(new StringBuilder("Total: ").append((categoriaProductoList.get(position).getProduprecio()*newValue)-(categoriaProductoList.get(position).getProduporcentaje()*newValue)).append("Bs."));
                 //precioenvio = (categoriaProductoList.get(position).getProduprecio()*newValue)-(categoriaProductoList.get(position).getProduporcentaje()*newValue);


            }
        });


        builder.setView(view);
        builder.setNeutralButton("DESCRIPCIÓN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mostrarDescripcion(position);
                dialog.dismiss();

            }
        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("ADICIONAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                double price = (Double.parseDouble(categoriaProductoList.get(position).getProduprecio().toString())*Double.parseDouble(txt_contador.getNumber()))-(categoriaProductoList.get(position).getProduporcentaje()*Double.parseDouble(txt_contador.getNumber()));
                final double finalprice = Math.round(price);
                //Toast.makeText(context, "Se agrego al carrito"+finalprice, Toast.LENGTH_SHORT).show();

                //-------------comprobar si hay ya el producto en room y si hay sumarlo o de lo contrario agregar cart------///
                int exitsItem = Common.cartRepository.countCartItems();
                final int idproducto = categoriaProductoList.get(position).getIdprodu();

                if(exitsItem>0){

                    boolean exist_id = Common.cartRepository.getIdProducto(idproducto);
                    Log.d("existe_idproducto", String.valueOf(exist_id));

                    if( exist_id){
                        try {
                            //cartList.get(position).producto==

                            //sumar a cart existentes


                                    Common.cartRepository.updateProducto(
                                            Integer.parseInt(txt_contador.getNumber()),
                                            Integer.parseInt(txt_contador.getNumber()),
                                            finalprice,
                                            finalprice,
                                            categoriaProductoList.get(position).getProduoferta(),
                                            categoriaProductoList.get(position).getProduporcentaje(),
                                            idproducto


                                    );


                            /*Cart cartItem = new Cart();
                            //cartItem.nombre = txt_nombre_producto.getText().toString();
                            //cartItem.nombre = categoriaProductoList.get(position).getProdunombre();
                            cartItem.amount = cartList.get(position).amount+ Integer.parseInt(txt_contador.getNumber());
                            cartItem.cantidad = cartList.get(position).cantidad+Integer.parseInt(txt_contador.getNumber());
                            //cartItem.producto = categoriaProductoList.get(position).getIdprodu();
                            //cartItem.precio = Double.parseDouble(lstproductos.get(position).precionuevo);
                            cartItem.precio = cartList.get(position).precio+ finalprice;
                            //cartItem.precio_uni =  categoriaProductoList.get(position).getProduprecio();
                            cartItem.precio_total = cartList.get(position).precio_total+ finalprice;
                            //cartItem.imgnombre = Common.BASE_URL+url+categoriaProductoList.get(position).getImgnombreprodu();


                            //Adicionar la base de datos

                            Common.cartRepository.upadateCart(cartItem);
                            Log.d("JCM_DEBUG",new Gson().toJson(cartItem));*/



                            Toast.makeText(context, "Se actualizo el carrito", Toast.LENGTH_SHORT).show();

                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(context,ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }



                    }else {

                        boolean existe = Boolean.parseBoolean(String.valueOf(Common.cartRepository.getCartItemByIdProducto(idproducto )) );
                        Log.d("EXISTE_NO", String.valueOf(existe)  );

                        //----------------------sino existe crea item----------//
                        try {


                            //crear nuevo cart

                            Cart cartItem = new Cart();
                            //cartItem.nombre = txt_nombre_producto.getText().toString();
                            cartItem.nombre = categoriaProductoList.get(position).getProdunombre();
                            cartItem.amount = Integer.parseInt(txt_contador.getNumber());
                            cartItem.cantidad = Integer.parseInt(txt_contador.getNumber());
                            cartItem.producto = categoriaProductoList.get(position).getIdprodu();
                            //cartItem.precio = Double.parseDouble(lstproductos.get(position).precionuevo);
                            cartItem.precio = finalprice;
                            cartItem.precio_uni = categoriaProductoList.get(position).getProduprecio();
                            cartItem.precio_total = finalprice;
                            cartItem.oferta = categoriaProductoList.get(position).getProduoferta();
                            cartItem.porcentaje_des = categoriaProductoList.get(position).getProduporcentaje();
                            cartItem.imgnombre = Common.BASE_URL+url+categoriaProductoList.get(position).getImgnombreprodu();


                            //Adicionar la base de datos

                            Common.cartRepository.insertToCart(cartItem);
                            Log.d("JCM_DEBUG",new Gson().toJson(cartItem));



                            Toast.makeText(context, "Se agrego al carrito", Toast.LENGTH_SHORT).show();




                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(context,ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }






                }else {

                    try {


                        //crear nuevo cart

                        Cart cartItem = new Cart();
                        //cartItem.nombre = txt_nombre_producto.getText().toString();
                        cartItem.nombre = categoriaProductoList.get(position).getProdunombre();
                        cartItem.amount = Integer.parseInt(txt_contador.getNumber());
                        cartItem.cantidad = Integer.parseInt(txt_contador.getNumber());
                        cartItem.producto = categoriaProductoList.get(position).getIdprodu();
                        //cartItem.precio = Double.parseDouble(lstproductos.get(position).precionuevo);
                        cartItem.precio = finalprice;
                        cartItem.precio_uni = categoriaProductoList.get(position).getProduprecio();
                        cartItem.precio_total = finalprice;
                        cartItem.oferta = categoriaProductoList.get(position).getProduoferta();
                        cartItem.porcentaje_des = categoriaProductoList.get(position).getProduporcentaje();
                        cartItem.imgnombre = Common.BASE_URL+url+categoriaProductoList.get(position).getImgnombreprodu();


                        //Adicionar la base de datos

                        Common.cartRepository.insertToCart(cartItem);
                        Log.d("JCM_DEBUG",new Gson().toJson(cartItem));



                        Toast.makeText(context, "Se agrego al carrito", Toast.LENGTH_SHORT).show();



                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(context,ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }




            }
        });
        builder.show();
    }

    private void mostrarDescripcion(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.add_carrito_descrip_layout,null);
        ImageView img_producto_dialog = view.findViewById(R.id.img_descri_producto_cart_id);
        final TextView txt_nombre_descrip_producto = view.findViewById(R.id.txt_dialogdescripProducto_nombre);

        //set datos
        Picasso.with(context)
                .load(Common.BASE_URL+url+categoriaProductoList.get(position).getImgnombreprodu())
                .into(img_producto_dialog);

        txt_nombre_descrip_producto.setText(categoriaProductoList.get(position).getProdudescripcion());


        builder.setView(view);
        builder.setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        builder.show();

    }

    @Override
    public int getItemCount() {
        return categoriaProductoList.size();
    }

    //-------------para actualizar badge----------------//


   /* private void actualizarCartCount() {


                if(Common.cartRepository.countCartItems() ==0)
                    badge.setVisibility(View.INVISIBLE);
                else {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(Common.cartRepository.countCartItems()));
                }

    }*/
    //--------------------------------------------------//


}
