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
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class NuevaCitaAdapter extends RecyclerView.Adapter<NuevaCitaAdapter.MyViewHolder> implements Filterable {
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
                LayoutInflater.from(parent.getContext()).inflate(R.layout.itemr,
                        parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.radio.setText(pacientesFilter.get(position).getNombre()+" "+pacientesFilter.get(position).getApellidos());
        holder.radio.setOnClickListener(new View.OnClickListener() {
            //Almacenar la respuesta elegida
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NuevaCita2Activity.class);
                intent.putExtra("nombre_paciente", pacientesFilter.get(position).getNombre()+" "+pacientesFilter.get(position).getApellidos());
                intent.putExtra("id_paciente",pacientesFilter.get(position).getId());
                intent.putExtra("id_paciente",pacientesFilter.get(position).getId());
                context.startActivity(intent);
            }
        });;
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
        RadioButton radio;

        public MyViewHolder(View v) {
            super(v);
            radio = (RadioButton) v.findViewById(R.id.radio);
        }


    }

    public NuevaCitaAdapter(ArrayList<Paciente> pacientes) {
        this.pacientes = pacientes;
        this.pacientesFilter = new ArrayList<>();
        this.pacientesFilter.addAll(pacientes);
        this.mFilter = new NuevaCitaAdapter.FiltroPacientes(NuevaCitaAdapter.this);
    }

    public class FiltroPacientes extends Filter {
        private NuevaCitaAdapter pacientesAdapter;

        private FiltroPacientes(NuevaCitaAdapter pacientesAdapter) {
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


