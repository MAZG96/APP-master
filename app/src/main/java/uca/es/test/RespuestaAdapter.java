package uca.es.test;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.ArrayList;

public class RespuestaAdapter extends RecyclerView.Adapter<RespuestaAdapter.MyViewHolder>{
    private ArrayList<Respuesta> respuestas;
    private RespuestaActivity res;
    private String resp;
    boolean nueva_respuesta=true;

    @NonNull
    @Override
    public RespuestaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int
            viewType) {
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.itemr,
                        parent, false);
        RespuestaAdapter.MyViewHolder vh = new RespuestaAdapter.MyViewHolder(v);
        Context context = parent.getContext();
        return vh;
    }

    public void onBindViewHolder(RespuestaAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if(resp.compareTo(respuestas.get(position).getTexto()) == 0){
            holder.radio.setChecked(true);
            nueva_respuesta=false;
        }
        holder.radio.setText(respuestas.get(position).getTexto());
        holder.radio.setOnClickListener(new View.OnClickListener() {
            //Almacenar la respuesta elegida
            @Override
            public void onClick(View v) {

                if(nueva_respuesta) {
                    res.introducir_respuesta(respuestas.get(position));
                }else{
                    res.actualizar_respuesta(respuestas.get(position));
                    nueva_respuesta=true;
                }
                res.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return respuestas.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton radio;

        public MyViewHolder(View v) {
            super(v);
            radio = (RadioButton) v.findViewById(R.id.radio);
        }


    }

        public RespuestaAdapter(RespuestaActivity respuestaActivity, ArrayList<Respuesta> myDataset,String id_p,String rep) {
        respuestas = myDataset;
        res = respuestaActivity;
            resp=rep;

    }
}
