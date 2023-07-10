package app.micromarket.marza.homeserviceoficial.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import app.micromarket.marza.homeserviceoficial.R;

public class ImgCategoriaViewHolder extends RecyclerView.ViewHolder {

    ImageView img_categoria;

    public ImgCategoriaViewHolder(View itemView) {
        super(itemView);

        img_categoria = (ImageView) itemView.findViewById(R.id.categoria_img_id);
    }
}
