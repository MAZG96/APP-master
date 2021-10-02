package uca.es.test;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MenuActivity extends AppCompatActivity {
    RequestQueue mRequestQueue;
    private int alarmID = 1;

    ArrayList<Cita> citas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mRequestQueue = Volley.newRequestQueue(this);

        Toolbar barra = (Toolbar) findViewById(R.id.my_toolbar);
        barra.setTitle("Menú Principal");
        setSupportActionBar(barra);

        //Introduce las citas en una alarma para notificar al usuario de sus citas
        notificar_citas();


        Button bpaciente = (Button) findViewById(R.id.bpacientes);
        Button bcita = (Button) findViewById(R.id.bcitas);


        //Pulsa el botón para acceder a CitasActivity
        bcita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CitasActivity.class);
                intent.putExtra("id_usuario","0");
                startActivity(intent);
            }
        });

        //Pulsa el botón para acceder a información personal
        bpaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, PacienteActivity.class);
                startActivity(intent);
            }
        });
    }



    public void notificar_citas() {
        String url = "https://playasconil.es/tfg/mostrar_citas.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("cita");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String fecha= hit.getString("fecha");
                                int number = hit.getInt("id");
                                int id_paciente = hit.getInt("id_paciente");

                                Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH).parse(fecha);
                                long milliseconds = date.getTime();
                                // si la fecha aún no ha pasado se activa en las alarmas
                                if(!date.before(new Date())) {
                                    long margen_aviso = 3600000; //1 hora = 3600000 milisegundos
                                    Utils.setAlarm(alarmID, milliseconds-margen_aviso, MenuActivity.this);
                                }


                                citas.add(new Cita(fecha,number,id_paciente));
                            }

                        } catch (JSONException | ParseException e) {
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
