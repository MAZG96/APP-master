package uca.es.test;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

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

public class CraneoActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RequestQueue mRequestQueue;
    private ArrayList<Pregunta> preguntas = new ArrayList<Pregunta>();
    private TextView trespuesta;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craneo);

        mRequestQueue = Volley.newRequestQueue(this);
        //TITULO TOOLBAR
        Toolbar barra = (Toolbar) findViewById(R.id.toolbarp);
        barra.setTitle("Cráneo: "+getIntent().getStringExtra("nombre")+" "+getIntent().getStringExtra("apellidos"));
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

        mostrar_preguntas();



    }

    public void mostrar_preguntas() {
        String url = "https://playasconil.es/tfg/selectAllJSON.php?seccion=7";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("pregunta");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String texto= hit.getString("cuestion");
                                int number = hit.getInt("id");
                                int seccion = hit.getInt("id_seccion");
                                int tipo = hit.getInt("tipo");
                                preguntas.add(new Pregunta(texto,number,seccion,tipo));
                            }
                            mostrar_respuestas_introducidas(preguntas);
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


    public void mostrar_respuestas_introducidas(final ArrayList<Pregunta> copia_preguntas){

        final String id_paciente = getIntent().getStringExtra("id_paciente");
        String url = "https://playasconil.es/tfg/selectAllrespuesta.php?id_paciente="+id_paciente;
        final ArrayList<Respuesta> respondidas = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("respuesta");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                int id_pregunta = hit.getInt("id_pregunta");
                                for(int k=0;k<copia_preguntas.size();k++) {
                                    if (id_pregunta == copia_preguntas.get(k).getNumber()) {

                                        if (copia_preguntas.get(k).getRespuesta() != "") {
                                            copia_preguntas.get(k).setRespuesta(copia_preguntas.get(k).getRespuesta() + " | " + hit.getString("texto"));
                                        } else {
                                            copia_preguntas.get(k).setRespuesta(hit.getString("texto"));
                                        }
                                    }
                                }

                            }
                            PreguntaAdapter mAdapter = new PreguntaAdapter(preguntas,id_paciente);
                            mRecyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { ;



                PreguntaAdapter mAdapter = new PreguntaAdapter(preguntas,id_paciente);
                mRecyclerView.setAdapter(mAdapter);

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
                preguntas.clear();
                mostrar_preguntas();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}