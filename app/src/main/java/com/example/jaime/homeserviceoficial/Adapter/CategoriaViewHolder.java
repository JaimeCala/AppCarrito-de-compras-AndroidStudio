package com.example.jaime.homeserviceoficial.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jaime.homeserviceoficial.Interface.ItemClickListener;
import com.example.jaime.homeserviceoficial.R;

public class CategoriaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ImageView img_categoria;
    TextView txt_categoria_nombre;

    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CategoriaViewHolder(View itemView) {
        super(itemView);

        img_categoria = (ImageView) itemView.findViewById(R.id.categoria_img_id);
        txt_categoria_nombre = (TextView)itemView.findViewById(R.id.categoria_title_id);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v);
    }
}
