package uca.es.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InformeActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RequestQueue mRequestQueue;
    private ArrayList<Informe> informes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);
        mRequestQueue = Volley.newRequestQueue(this);
        mostrar_informes();
        //TITULO TOOLBAR
        Toolbar barra = (Toolbar) findViewById(R.id.toolbarp);
        barra.setTitle("Informes: " + getIntent().getStringExtra("nombre") + " " + getIntent().getStringExtra("apellidos"));
        setSupportActionBar(barra);
// Referenciamos al RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
// Mejoramos rendimiento con esta configuración
        mRecyclerView.setHasFixedSize(true);
// Creamos un LinearLayoutManager para gestionar el item.xml creado antes
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
// Lo asociamos al RecyclerView
        mRecyclerView.setLayoutManager(mLayoutManager);
// Creamos un ArrayList de preguntas


    }

    public void mostrar_informes() {
        String url = "https://playasconil.es/tfg/mostrar_informes.php?id_paciente="+getIntent().getStringExtra("id_paciente");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("informes");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                int id= hit.getInt("id");
                                int id_paciente = hit.getInt("id_paciente");
                                String url = hit.getString("direccion");
                                String nombre = hit.getString("nombre_archivo");
                                informes.add(new Informe(id,id_paciente,url,nombre));
                            }

                            InformeAdapter mAdapter = new InformeAdapter(informes);
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
                informes.clear();
                mostrar_informes();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}