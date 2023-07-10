package app.micromarket.marza.homeserviceoficial.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.micromarket.marza.homeserviceoficial.Model.ImgCategoria;
import app.micromarket.marza.homeserviceoficial.R;
import app.micromarket.marza.homeserviceoficial.Retrofit.ICarritoShopAPI;
import app.micromarket.marza.homeserviceoficial.Utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImgCategoriaAdapter extends RecyclerView.Adapter<ImgCategoriaViewHolder> {

    Context context;
    //List<Categoria> categoriaList;
    List<ImgCategoria> imgCategoriaList;
    ICarritoShopAPI mService;

    public ImgCategoriaAdapter(Context context, List<ImgCategoria> imgCategoriaList) {
        this.context = context;
        this.imgCategoriaList = imgCategoriaList;
    }

    @Override
    public ImgCategoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.categoria_menu_item_layout,null);
        return new ImgCategoriaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImgCategoriaViewHolder holder, int position) {

        mService = Common.getAPI();
        String url= "http://192.168.43.34:3000/api/img-categoria/";

        //cargamos imagen
        Picasso.with(context)
                .load(url+imgCategoriaList.get(position).nombreimgcategoria)
                .into(holder.img_categoria);
        //holder.txt_categoria_nombre.setText(categoriaList.get(position).nombre);
        /*Picasso.with(context)
                .load(new File("file://"+url+ imgCategoriaList.get(position).linkimgcategoria) )
                .into(holder.img_categoria);*/

        /*Picasso.with(context)
                .load(new File(url+ imgCategoriaList.get(position).linkimgcategoria) )
                .into(holder.img_categoria);*/
        /*Picasso.with(context)
                .load(new File("file://"+url+ imgCategoriaList.get(position).linkimgcategoria) )
                .into(holder.img_categoria);*/

    }

    @Override
    public int getItemCount() {
        return imgCategoriaList.size();
    }
}
