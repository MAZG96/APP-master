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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;


public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.MyViewHolder> implements Filterable {
    private ArrayList<Cita> citas;
    private ArrayList<Cita> citasFilter;
    private FiltroCitas mFilter;
    private Context context;
    private String id_usuario;

    @Override
    public int getItemCount() {
        return citasFilter.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fecha_cita;
        TextView nombre_paciente;
        TextView valoracion;
        Button show;

        public MyViewHolder(View v) {
            super(v);
            fecha_cita= (TextView) v.findViewById(R.id.tNombreArchivo);
            nombre_paciente= (TextView) v.findViewById(R.id.tnombre_paciente);
            valoracion = (TextView) v.findViewById(R.id.tval);
            show = (Button) v.findViewById(R.id.show);
        }


    }

    public CitasAdapter(ArrayList<Cita> citas,String id_usuario) {
        this.citas = citas;
        this.citasFilter = new ArrayList<>();
        this.citasFilter.addAll(citas);
        this.mFilter = new CitasAdapter.FiltroCitas(CitasAdapter.this);
        this.id_usuario=id_usuario;
    }

    public Filter getFilter() {
        return mFilter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int
            viewType) {
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cita,
                        parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.fecha_cita.setText(citasFilter.get(position).getFecha());
        holder.nombre_paciente.setText(citasFilter.get(position).getNombre_paciente());
        holder.valoracion.setText(citasFilter.get(position).getValoracion());

        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mostrar opciones de cada cita
                Intent intent = new Intent(context, DetallesCitaActivity.class);
                intent.putExtra("titulo",citasFilter.get(position).getFecha());
                intent.putExtra("id_cita",""+citasFilter.get(position).getNumber());
                intent.putExtra("id_paciente",citasFilter.get(position).getId_paciente());
                intent.putExtra("valoracion",citasFilter.get(position).getValoracion());
                intent.putExtra("id_usuario",id_usuario);


                context.startActivity(intent);

            }
        });
    }



    /*Filtro*/
    public class FiltroCitas extends Filter {
        private CitasAdapter citasAdapter;

        private FiltroCitas(CitasAdapter citasAdapter) {
            super();
            this.citasAdapter = citasAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            citasFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                citasFilter.addAll(citas);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Cita cita : citas) {
                    if (cita.getFecha().toLowerCase().contains(filterPattern) || cita.getNombre_paciente().toLowerCase().contains(filterPattern)) {
                        citasFilter.add(cita);
                    }
                }
                }

            results.values = citasFilter;
            results.count = citasFilter.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.citasAdapter.notifyDataSetChanged();

        }
    }
}
