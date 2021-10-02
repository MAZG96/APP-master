package uca.es.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NuevaCitaActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RequestQueue mRequestQueue;
    private ArrayList<Paciente> pacientes = new ArrayList<Paciente>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cita);
        //TITULO TOOLBAR
        Toolbar barra = (Toolbar) findViewById(R.id.toolbarp);
        barra.setTitle("Elige paciente");
        setSupportActionBar(barra);
// Referenciamos al RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
// Mejoramos rendimiento con esta configuración
        mRecyclerView.setHasFixedSize(true);
// Creamos un LinearLayoutManager para gestionar el item_paciente.xml creado antes
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
// Lo asociamos al RecyclerView
        mRecyclerView.setLayoutManager(mLayoutManager);
// Creamos un ArrayList de pacientes
        EditText eBuscar = (EditText) findViewById(R.id.eBuscar);
// JSON PARSE STRING
        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();

        final NuevaCitaAdapter pacientesAdapter = new NuevaCitaAdapter(pacientes);
        mRecyclerView.setAdapter(pacientesAdapter);


        eBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mRecyclerView.setAdapter(pacientesAdapter);
                pacientesAdapter.getFilter().filter(s.toString());

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void parseJSON() {
        String url = "https://playasconil.es/tfg/mostrar_pacientes.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("paciente");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                int id= hit.getInt("id");
                                String nombre= hit.getString("nombre");
                                String apellidos= hit.getString("apellidos");
                                String fecha_nacimiento= hit.getString("fecha_nacimiento");
                                String telefono= hit.getString("telefono");

                                pacientes.add(new Paciente(id,nombre,apellidos,fecha_nacimiento,telefono));
                            }

                            mAdapter = new NuevaCitaAdapter(pacientes);
                            mRecyclerView.setAdapter(mAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.atras:
                //Vuelve a la activity anterior
                finish();

                return true;

            case R.id.recarga:
                //Vuelve a la activity anterior
                pacientes.clear();
                parseJSON();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

