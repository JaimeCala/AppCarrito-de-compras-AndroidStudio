package com.example.jaime.homeserviceoficial.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jaime.homeserviceoficial.Database.ModelDB.Cart;
import com.example.jaime.homeserviceoficial.Model.CategoriaProducto;
import com.example.jaime.homeserviceoficial.Model.Historial;
import com.example.jaime.homeserviceoficial.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
