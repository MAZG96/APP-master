package uca.es.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TratamientoActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    static String rest;
    RequestQueue mRequestQueue;
    ArrayList<Respuesta> respuestas = new ArrayList<Respuesta>();
    ArrayList<String> respuestas_contestadas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuesta);

        String titulo = "Tratamientos";
        //TITULO TOOLBAR
        Toolbar barra = (Toolbar) findViewById(R.id.toolbarp);
        barra.setTitle(titulo);
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





// JSON PARSE STRING
        mRequestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String id_pregunta = "58";
        String id_paciente = getIntent().getStringExtra("id_paciente");;
        mostrar_respuestas_introducidas(id_pregunta,id_paciente);

    }

    private void parseJSON(final ArrayList<Respuesta> respondidas) {
        String url = "https://playasconil.es/tfg/respuestaJSON.php?id_pregunta=58";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("opcion");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String texto= hit.getString("opcion");
                                int number = hit.getInt("id");

                                respuestas.add(new Respuesta(texto,number));
                            }
                            String id_paciente = getIntent().getStringExtra("id_paciente");
                            mAdapter = new TratamientoAdapter(TratamientoActivity.this, respuestas,id_paciente,respondidas);
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

    public void mostrar_respuestas_introducidas(String id_pregunta,String id_paciente){

        String url = "https://playasconil.es/tfg/getRespuesta.php?id_pregunta="+id_pregunta+"&id_paciente="+id_paciente;

        final ArrayList<Respuesta> respondidas = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("respuesta");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                int id = hit.getInt("id");
                                String texto = hit.getString("texto");
                                respondidas.add(new Respuesta(texto,id));

                            }
                            parseJSON(respondidas);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                parseJSON(null);
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);

    }


    //ELIMINAR RESPUESTAS
    public void eliminar_respuestas(String texto_respuesta){
        String id_pregunta = "58";
        String id_paciente = getIntent().getStringExtra("id_paciente");
        String url = "https://playasconil.es/tfg/eliminar_respuesta_multiple.php?id_paciente="+id_paciente+"&id_pregunta="+id_pregunta+"&texto_respuesta="+texto_respuesta;

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                });

        mRequestQueue.add(deleteRequest);
    }





    public void introducir_respuesta(final Respuesta r){
        final String txt = r.getTexto();
        final String s = "58";
        final String p = getIntent().getStringExtra("id_paciente");
        String url = "https://playasconil.es/tfg/insertar_respuesta.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("id_pregunta", s);
                params.put("id_paciente", p);
                params.put("respuesta", txt);

                return params;
            }
        };
        mRequestQueue.add(postRequest);
    }

    public void actualizar_respuesta(final Respuesta r){
        final String txt = r.getTexto();
        final String s = getIntent().getStringExtra("id_pregunta");
        final String p = getIntent().getStringExtra("id_paciente");
        String url = "https://playasconil.es/tfg/actualizar_respuesta.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("id_pregunta", s);
                params.put("id_paciente", p);
                params.put("respuesta", txt);

                return params;
            }
        };
        mRequestQueue.add(postRequest);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.atras:
                //Vuelve a la activity anterior
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}