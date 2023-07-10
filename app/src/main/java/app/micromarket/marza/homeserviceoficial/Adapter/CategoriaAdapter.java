package app.micromarket.marza.homeserviceoficial.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.micromarket.marza.homeserviceoficial.Interface.ItemClickListener;
import app.micromarket.marza.homeserviceoficial.Model.Categoria;
import app.micromarket.marza.homeserviceoficial.Model.ImgCategoria;
import app.micromarket.marza.homeserviceoficial.ProductoActivity;
import app.micromarket.marza.homeserviceoficial.R;
import app.micromarket.marza.homeserviceoficial.Retrofit.ICarritoShopAPI;
import app.micromarket.marza.homeserviceoficial.Utils.Common;
import com.squareup.picasso.Picasso;

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
