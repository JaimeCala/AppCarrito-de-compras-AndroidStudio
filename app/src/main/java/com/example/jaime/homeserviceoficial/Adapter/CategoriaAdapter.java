package com.example.jaime.homeserviceoficial.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jaime.homeserviceoficial.Interface.ItemClickListener;
import com.example.jaime.homeserviceoficial.Model.Categoria;
import com.example.jaime.homeserviceoficial.Model.ImgCategoria;
import com.example.jaime.homeserviceoficial.ProductoActivity;
import com.example.jaime.homeserviceoficial.R;
import com.example.jaime.homeserviceoficial.Retrofit.ICarritoShopAPI;
import com.example.jaime.homeserviceoficial.Utils.Common;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaViewHolder> {

    Context context;
    List<Categoria> categoriaList;
    List <ImgCategoria> imgCategoriaList;
    ICarritoShopAPI mService;

    public CategoriaAdapter(List<ImgCategoria> imgCategoriaList) {
        this.imgCategoriaList = imgCategoriaList;
    }

    public CategoriaAdapter(Context context, List<Categoria> categoriaList) {
        this.context = context;
        this.categoriaList = categoriaList;



    }

    @Override
    public CategoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.categoria_menu_item_layout,null);
        return new CategoriaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoriaViewHolder holder, final int position) {

        mService = Common.getAPI();
        String url= "api/img-categoria/";

        //iteramos la respuesta imgcategoria
        List<Categoria.ImgCategoria> arrayList = categoriaList.get(position).getImgcategorias();
        for(int i = 0; i<arrayList.size();i++)
        {

            //cargamos imagen
            Picasso.with(context)
                    //.load(url+categoriaList.get(position).getNombreimgcategoria())
                    .load(Common.BASE_URL+url+arrayList.get(i).getNombreimgcategoria())


                    .into(holder.img_categoria);
        }

        holder.txt_categoria_nombre.setText(categoriaList.get(position).getNombre());

        //eventos
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v) {
                Common.currentCategory = categoriaList.get(position);

                //iniciar la nueva actividad
                context.startActivity(new Intent(context, ProductoActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }
}
