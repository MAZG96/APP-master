package uca.es.test;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetallesCitaActivity extends AppCompatActivity {
    private TextView tnombre;
    private TextView ttelefono;
    private TextView tvaloracion;
    private RequestQueue mRequestQueue;
    private RatingBar valoracion;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_cita);

        Toolbar barra = (Toolbar) findViewById(R.id.toolbarp);
        setSupportActionBar(barra);
        tnombre = (TextView) findViewById(R.id.tnombre);
        ttelefono = (TextView) findViewById(R.id.ttelefono);
        tvaloracion = (TextView) findViewById(R.id.tvaloracion);
        TextView tfecha = (TextView) findViewById(R.id.tfecha);
        Button bEliminar = (Button) findViewById(R.id.bEliminar);
        Button bModificar = (Button) findViewById(R.id.bModificar);
        valoracion = (RatingBar) findViewById(R.id.ratingBar);

        ttelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(DetallesCitaActivity.this);
                builder.setMessage("¿Desea llamar al paciente?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                if (Build.VERSION.SDK_INT > 22) {

                                    if (ActivityCompat.checkSelfPermission(DetallesCitaActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                        ActivityCompat.requestPermissions(DetallesCitaActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

                                        return;
                                    }
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:+34" + ttelefono.getText().toString().trim()));
                                    startActivity(callIntent);
                                } else {

                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:+34" + ttelefono.getText().toString().trim()));
                                    startActivity(callIntent);
                                }

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




        //mensaje de confirmacion

        AlertDialog.Builder builder = new AlertDialog.Builder(DetallesCitaActivity.this);
        builder.setMessage("¿Quieres eliminar la cita?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        eliminar_cita();

                        startActivity(intent);

                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        final AlertDialog dialog = builder.create();


        bEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                System.out.println("boton eliminar");


            }
        });

        bModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallesCitaActivity.this, NuevaCita2Activity.class);
                String id_cita = getIntent().getStringExtra("id_cita");
                intent.putExtra("id_cita",id_cita);
                intent.putExtra("id_paciente",getIntent().getIntExtra("id_paciente",0));
                startActivity(intent);
            }
        });

        if(!getIntent().getStringExtra("valoracion").isEmpty()) {
            valoracion.setRating(Float.parseFloat(getIntent().getStringExtra("valoracion")));
        }

        if(getIntent().getStringExtra("valoracion") == "0"){
            valoracion.setIsIndicator(true);
        }

        valoracion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            // Called when the user swipes the RatingBar
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String id_cita = getIntent().getStringExtra("id_cita");
                insertar_valoracion(String.valueOf(valoracion.getRating()),id_cita);
            }
        });

        System.out.println("id_suusario: "+getIntent().getStringExtra("id_usuario"));

        String fechahora = getIntent().getStringExtra("titulo");

        if(!getIntent().getStringExtra("id_usuario").equals("0")) {
            bModificar.setVisibility(View.INVISIBLE);
            bEliminar.setVisibility(View.INVISIBLE);
            try {

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            Date fecha_cita = null;

            fecha_cita = formatter.parse(fechahora);

            LocalDateTime now = LocalDateTime.now();
            Date fecha_actual = formatter.parse(formatter.format(new Date()));

            if (fecha_cita.after(fecha_actual)) {
                    valoracion.setIsIndicator(true);
                    tvaloracion.setText("Aún no ha recibido la cita");
                }else{
                tvaloracion.setText("El paciente puede valorar la cita recibida");
                valoracion.setIsIndicator(false);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }else{
            valoracion.setIsIndicator(true);

        }

        mRequestQueue = Volley.newRequestQueue(this);

        String id = Integer.toString(getIntent().getIntExtra("id_paciente",2));
        mostrar_paciente(id);
        fechahora = getIntent().getStringExtra("titulo");
        tfecha.setText(fechahora);

    }

    private void eliminar_cita() {
        String url = "https://playasconil.es/tfg/eliminar_cita.php?id="+getIntent().getStringExtra("id_cita");

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

    public void insertar_valoracion(final String val, final String id_cita) {
        String url = "https://playasconil.es/tfg/insertar_valoracion.php";
        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
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
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("valoracion", val);
                params.put("id_cita",id_cita);

                return params;
            }

        };

        mRequestQueue.add(putRequest);
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
                                tnombre.setText(nombre+" "+apellidos);
                                ttelefono.setText(telefono);

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