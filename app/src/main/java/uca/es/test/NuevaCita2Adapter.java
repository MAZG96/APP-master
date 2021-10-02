package uca.es.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.ArrayList;

public class NuevaCita2Adapter extends RecyclerView.Adapter<NuevaCita2Adapter.MyViewHolder>{
    private ArrayList<Cita> citas;
    private Context context;
    private int id_paciente;
    private String id_cita;

    public NuevaCita2Adapter(NuevaCita2Activity ncActivity, ArrayList<Cita> myDataset) {
        citas = myDataset;

    }

    public NuevaCita2Adapter(ArrayList<Cita> horas,int idp,String idc) {
        citas=horas;
        id_paciente=idp;
        id_cita=idc;
    }

    @NonNull
    @Override
    public NuevaCita2Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int
            viewType) {
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.itemr,
                        parent, false);
        NuevaCita2Adapter.MyViewHolder vh = new NuevaCita2Adapter.MyViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    public void onBindViewHolder(NuevaCita2Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        System.out.println("ID:          "+id_paciente);

        if(citas.get(position).getId_paciente() != 0){
            holder.radio.setText(citas.get(position).getFecha()+" OCUPADA");
            holder.radio.setEnabled(false);
        }else{
            holder.radio.setText(citas.get(position).getFecha());
        }
        holder.radio.setOnClickListener(new View.OnClickListener() {
            //Almacenar la respuesta elegida
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConfirmarCitaActivity.class);
                intent.putExtra("id_paciente",id_paciente);
                intent.putExtra("fecha",citas.get(position).getFecha());
                intent.putExtra("id_cita",id_cita);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return citas.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton radio;

        public MyViewHolder(View v) {
            super(v);
            radio = (RadioButton) v.findViewById(R.id.radio);
        }


    }



}
