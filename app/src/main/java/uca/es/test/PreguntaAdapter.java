package uca.es.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class PreguntaAdapter extends RecyclerView.Adapter<PreguntaAdapter.MyViewHolder> {
    private ArrayList<Pregunta> preguntas;
    private Context context;
    private String id_paciente;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int
            viewType) {
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pregunta,
                        parent, false);
        MyViewHolder vh = new MyViewHolder(v);

        context = parent.getContext();
        return vh;
    }

    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.name.setText(preguntas.get(position).getTexto());

        holder.respuesta.setText(preguntas.get(position).getRespuesta());

        if(preguntas.get(position).getTipo() == 0){ //Preguntas de una sola respuesta
        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mostrar opciones de cada pregunta
                Intent intent = new Intent(context, RespuestaActivity.class);
                intent.putExtra("titulo",preguntas.get(position).getTexto());
                intent.putExtra("id_pregunta",""+preguntas.get(position).getNumber());
                intent.putExtra("id_paciente", id_paciente);
                context.startActivity(intent);

            }
        });
        }else if(preguntas.get(position).getTipo() == 1){ //preguntas multirespuestas
            holder.show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Mostrar opciones de cada pregunta
                    Intent intent = new Intent(context, RespuestaMultipleActivity.class);
                    intent.putExtra("titulo",preguntas.get(position).getTexto());
                    intent.putExtra("id_pregunta",""+preguntas.get(position).getNumber());
                    intent.putExtra("id_paciente", id_paciente);
                    context.startActivity(intent);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return preguntas.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView respuesta;
        Button show;

        public MyViewHolder(View v) {
            super(v);

            respuesta = (TextView) v.findViewById(R.id.trespuesta);
            name = (TextView) v.findViewById(R.id.tNombreArchivo);
            show = (Button) v.findViewById(R.id.show);
        }


    }

    public PreguntaAdapter(ArrayList<Pregunta> myDataset,String id_p) {
        preguntas = myDataset;
        id_paciente=id_p;

    }


}
