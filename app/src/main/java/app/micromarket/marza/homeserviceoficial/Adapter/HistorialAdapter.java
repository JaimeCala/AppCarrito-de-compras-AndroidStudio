package app.micromarket.marza.homeserviceoficial.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.micromarket.marza.homeserviceoficial.Model.Historial;
import app.micromarket.marza.homeserviceoficial.R;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapterViewHolder> {


    Context context;
    List<Historial> historialList;

    public HistorialAdapter(Context context, List<Historial> historialList) {
        this.context = context;
        this.historialList = historialList;
    }

    @Override
    public HistorialAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.content_historial,null);
        return new HistorialAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HistorialAdapterViewHolder holder, int position) {




        String valor = String.valueOf(historialList.get(position).getFecha());
        String[] lista = valor.split("T");


        holder.txt_fechahis.setText(lista[0] );
        //holder.txt_fechahis.setText(String.valueOf(historialList.get(position).getFecha()) );
        holder.txt_preciohis.setText(String.valueOf(historialList.get(position).getPrecio()) );
        holder.txt_estadohis.setText(historialList.get(position).getEstado());



    }

    @Override
    public int getItemCount() {
       return historialList.size();

    }
}
