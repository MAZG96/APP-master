package uca.es.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InformeAdapter extends RecyclerView.Adapter<InformeAdapter.MyViewHolder> {
    private ArrayList<Informe> informes;
    private Context context;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int
            viewType) {
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_informe,
                        parent, false);
        MyViewHolder vh = new MyViewHolder(v);

        context = parent.getContext();
        return vh;
    }

    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.name.setText(informes.get(position).getNombre_archivo());

        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mostrar opciones de cada pregunta
                Intent intent = new Intent(context, DetallesInformeActivity.class);
                intent.putExtra("nombre_archivo",informes.get(position).getNombre_archivo());
                intent.putExtra("direccion",informes.get(position).getUrl());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return informes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        Button show;

        public MyViewHolder(View v) {
            super(v);

            name = (TextView) v.findViewById(R.id.tNombreArchivo);
            show = (Button) v.findViewById(R.id.show);
        }


    }

    public InformeAdapter(ArrayList<Informe> myDataset) {
        informes = myDataset;
    }


}
