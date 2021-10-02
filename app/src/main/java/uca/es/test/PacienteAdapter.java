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

public class PacienteAdapter extends RecyclerView.Adapter<PacienteAdapter.MyViewHolder> implements Filterable {
    private ArrayList<Paciente> pacientes;
    private ArrayList<Paciente> pacientesFilter;
    private FiltroPacientes mFilter;
    private Context context;
    private PacienteActivity pac;



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int
            viewType) {
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paciente,
                        parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.nombre.setText(String.valueOf(pacientesFilter.get(position).getNombre()+" "+pacientesFilter.get(position).getApellidos()));
        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mostrar opciones de cada paciente
                Intent intent = new Intent(context, Paciente_EditarActivity.class);
                intent.putExtra("id",String.valueOf(pacientesFilter.get(position).getId()));
                intent.putExtra("nombre",pacientesFilter.get(position).getNombre());
                intent.putExtra("apellidos",pacientesFilter.get(position).getApellidos());
                intent.putExtra("fecha_nacimiento",pacientesFilter.get(position).getFecha_nacimiento());
                intent.putExtra("telefono",pacientesFilter.get(position).getTelefono());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return pacientesFilter.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        TextView apellido;
        Button show;

        public MyViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.tnombre_paciente);
            apellido = (TextView) v.findViewById(R.id.apellido);
            show = (Button) v.findViewById(R.id.show);
        }


    }

    public PacienteAdapter(ArrayList<Paciente> pacientes) {
        this.pacientes = pacientes;
        this.pacientesFilter = new ArrayList<>();
        this.pacientesFilter.addAll(pacientes);
        this.mFilter = new PacienteAdapter.FiltroPacientes(PacienteAdapter.this);
    }

    public class FiltroPacientes extends Filter {
        private PacienteAdapter pacientesAdapter;

        private FiltroPacientes(PacienteAdapter pacientesAdapter) {
            super();
            this.pacientesAdapter = pacientesAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            pacientesFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                pacientesFilter.addAll(pacientes);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Paciente paciente : pacientes) {
                    if (paciente.getNombre().toLowerCase().contains(filterPattern) || paciente.getApellidos().toLowerCase().contains(filterPattern)) {
                        pacientesFilter.add(paciente);
                    }
                }
            }

            results.values = pacientesFilter;
            results.count = pacientesFilter.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.pacientesAdapter.notifyDataSetChanged();

        }
    }
}
