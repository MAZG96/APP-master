package uca.es.test;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmarCitaActivity extends AppCompatActivity {

    private TextView tpaciente;
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_cita);

        Toolbar barra = (Toolbar) findViewById(R.id.toolbarp);
        setSupportActionBar(barra);

        Button baceptar = (Button) findViewById(R.id.baceptar);
        Button bcancelar = (Button) findViewById(R.id.bcancelar);
        tpaciente = (TextView) findViewById(R.id.tpaciente);
        TextView tfecha = (TextView) findViewById(R.id.tfecha);

        tfecha.setText(getIntent().getStringExtra("fecha"));

        mRequestQueue = Volley.newRequestQueue(this);
        final Cita c = new Cita(getIntent().getStringExtra("fecha"),0,getIntent().getIntExtra("id_paciente",2));
        mostrar_paciente(Integer.toString(getIntent().getIntExtra("id_paciente",2)));

        baceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CitasActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                if(getIntent().getStringExtra("id_cita") != null){
                    modificar_cita(c);
                }else {
                    introducir_cita(c);
                }

                startActivity(intent);
            }
        });

        bcancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarCitaActivity.this);
                builder.setMessage("¿Desea cancelar el proceso de crear la cita?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                Intent intent = new Intent(getApplicationContext(), CitasActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        })
                ;
                final AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


    }

    private void mostrar_paciente(String id){

        String url = "https://playasconil.es/tfg/mostrar_paciente.php?id_paciente="+id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("paciente");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String nombre = hit.getString("nombre");
                                String apellidos = hit.getString("apellidos");
                                String telefono = hit.getString("telefono");
                                tpaciente.setText(nombre+" "+apellidos);


                            }




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

    public void introducir_cita(final Cita c) {
        final String fecha = c.getFecha();
        final String id_paciente = Integer.toString(c.getId_paciente());
        String url = "https://playasconil.es/tfg/insertar_cita.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fecha",fecha);
                params.put("id_paciente", id_paciente);

                return params;
            }
        };

        mRequestQueue.add(postRequest);
    }

    public void modificar_cita(final Cita c) {
        final String fecha = c.getFecha();
        final String id_paciente = Integer.toString(c.getId_paciente());
        final String id_cita = getIntent().getStringExtra("id_cita");
        String url = "https://playasconil.es/tfg/modificar_cita.php?id_cita="+id_cita;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fecha",fecha);

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