package app.micromarket.marza.homeserviceoficial.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.micromarket.marza.homeserviceoficial.Database.ModelDB.Favorite;
import app.micromarket.marza.homeserviceoficial.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    Context context;
    List<Favorite> favoriteList;

    public FavoriteAdapter(Context context, List<Favorite> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.fav_item_layout,parent,false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        Picasso.with(context).load(favoriteList.get(position).imgnombre).into(holder.img_fav);
        holder.txt_fav_product_precioUnidad.setText(new StringBuilder("BS.").append(favoriteList.get(position).precio).toString());
        holder.txt_fav_product_nombre.setText(favoriteList.get(position).nombre);

    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder{

        ImageView img_fav;
        TextView txt_fav_product_nombre,txt_fav_product_precioUnidad;

        //para eleminar

        public RelativeLayout view_background;
        public LinearLayout view_foreground;


        public FavoriteViewHolder(View itemView) {
            super(itemView);

            img_fav = itemView.findViewById(R.id.img_fav);
            txt_fav_product_nombre = itemView.findViewById(R.id.txt_fav_product_nombre);
            txt_fav_product_precioUnidad = itemView.findViewById(R.id.txt_fav_product_precioUnidad);

            view_background = itemView.findViewById(R.id.view_background);
            view_foreground = itemView.findViewById(R.id.view_foreground);
        }
    }
    public  void removeItem(int position)
    {
        favoriteList.remove(position);
        notifyItemRemoved(position);

    }
    public void restoreItem(Favorite item,int position)
    {
        favoriteList.add(position,item);
        notifyItemInserted(position);
    }

}
