package uca.es.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TratamientoAdapter extends RecyclerView.Adapter<TratamientoAdapter.MyViewHolder>{
    private ArrayList<Respuesta> respuestas;
    private TratamientoActivity res;
    private ArrayList<Respuesta> resp;
    boolean nueva_respuesta=true;

    @NonNull
    @Override
    public TratamientoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int
            viewType) {
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mulitple,
                        parent, false);
        TratamientoAdapter.MyViewHolder vh = new TratamientoAdapter.MyViewHolder(v);
        Context context = parent.getContext();
        return vh;
    }

    public void onBindViewHolder(final TratamientoAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        int posicion_respondida=0;
        if(resp != null){
            for(int i=0;i<resp.size();i++){
                if(resp.get(i).getTexto().compareTo(respuestas.get(position).getTexto()) == 0){
                    holder.bswitch.setChecked(true);
                    posicion_respondida=i;
                    nueva_respuesta=false;
                }
            }}

        final int finalPosicion_respondida = posicion_respondida;
        holder.bswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.bswitch.isChecked()){
                    //insertar respuesta
                    res.introducir_respuesta(respuestas.get(position));
                }else{

                    res.eliminar_respuestas(respuestas.get(position).getTexto());
                    //eliminar respuesta
                }

            }
        });


        holder.bswitch.setText(respuestas.get(position).getTexto());

    }

    @Override
    public int getItemCount() {
        return respuestas.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Switch bswitch;
        public MyViewHolder(View v) {
            super(v);
            bswitch = (Switch) v.findViewById(R.id.bswitch);

        }


    }

    public TratamientoAdapter(TratamientoActivity respuestamultipleActivity, ArrayList<Respuesta> myDataset,String id_p,ArrayList<Respuesta> rep) {
        respuestas = myDataset;
        res = respuestamultipleActivity;
        resp=rep;
    }
}

