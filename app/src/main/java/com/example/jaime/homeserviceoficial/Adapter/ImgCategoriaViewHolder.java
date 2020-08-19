package com.example.jaime.homeserviceoficial.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.jaime.homeserviceoficial.R;

public class ImgCategoriaViewHolder extends RecyclerView.ViewHolder {

    ImageView img_categoria;

    public ImgCategoriaViewHolder(View itemView) {
        super(itemView);

        img_categoria = (ImageView) itemView.findViewById(R.id.categoria_img_id);
    }
}
