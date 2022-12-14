package com.example.jaime.homeserviceoficial.Adapter;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jaime.homeserviceoficial.Interface.ItemClickListener;
import com.example.jaime.homeserviceoficial.R;

public class CategoriaProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ImageView img_producto;
    TextView txt_nombreProducto,txt_pesoProducto,txt_unidadProducto,txt_precioProducto, txt_precioOferta;

    TextView txt_preci_sum;

    ItemClickListener itemClickListener;

    ImageView btn_add_to_cart,btn_favorito;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CategoriaProductoViewHolder(View itemView) {
        super(itemView);


        img_producto =  itemView.findViewById(R.id.producto_img_id);
        txt_nombreProducto = itemView.findViewById(R.id.txt_nombreProducto_id);
        txt_pesoProducto = itemView.findViewById(R.id.txt_peso_id);
        txt_unidadProducto = itemView.findViewById(R.id.txt_unidad_id);
        txt_precioProducto = itemView.findViewById(R.id.txt_precio_id);
        txt_precioOferta = itemView.findViewById(R.id.txt_preciooferta_id);

        btn_add_to_cart= itemView.findViewById(R.id.boton_agregarCarrito_id);
        btn_favorito= itemView.findViewById(R.id.boton_favorito_id);






        itemView.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v);

    }
}
