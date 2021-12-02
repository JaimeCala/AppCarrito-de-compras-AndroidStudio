package com.example.jaime.homeserviceoficial.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jaime.homeserviceoficial.R;

public class HistorialAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView txt_fechahis,txt_preciohis,txt_estadohis;

    public HistorialAdapterViewHolder(View itemView) {
        super(itemView);

            txt_fechahis = itemView.findViewById(R.id.txt_fechahistorial);
            txt_preciohis = itemView.findViewById(R.id.txt_preciohistorial);
            txt_estadohis = itemView.findViewById(R.id.txt_estadohistorial);
    }

    @Override
    public void onClick(View v) {

    }
}
