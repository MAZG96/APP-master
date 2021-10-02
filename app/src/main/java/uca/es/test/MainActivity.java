package uca.es.test;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.content.ServiceConnection;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private int alarmID = 1;
    RequestQueue mRequestQueue;
    private EditText eusuario;
    private EditText epass;
    int id_paciente=0;





    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //declaramos la cola de peticiones HTTP
        mRequestQueue = Volley.newRequestQueue(this);



        //declaracion de los objetos del layout
        eusuario = (EditText) findViewById(R.id.eUsuario);
        epass = (EditText) findViewById(R.id.ePass);
        Button entrar = (Button) findViewById(R.id.button);

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usuario = String.valueOf(eusuario.getText());
                final String pass = String.valueOf(epass.getText());
                if(eusuario.getText().toString().isEmpty() || epass.getText().toString().isEmpty()) {
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "RELLENE LOS CAMPOS", Toast.LENGTH_SHORT);

                    toast1.show();
                }else {
                    Get_id_paciente(usuario, pass);
                }
            }
        });

    }

    public void Get_id_paciente(final String usuario, final String pass) {
        String url = "https://playasconil.es/tfg/mostrar_rol.php?usuario="+usuario;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("usuario");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                id_paciente = hit.getInt("id_paciente");
                                autenticar(usuario,pass,id_paciente);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                autenticar(usuario,pass,id_paciente);
            }
        });

        mRequestQueue.add(request);

    }

    public void autenticar(final String usuario, final String pass, final int id_paciente) {
        String url = "https://playasconil.es/tfg/autenticar_usuario.php";
        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response

                        Log.d("Response", response);
                        if(response.equals("Autenticado") && id_paciente ==0){ // ROL DE ADMIN
                            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                            startActivity(intent);

                        }else if(response.equals("Autenticado") && id_paciente !=0){ //ROL DE PACIENTE
                            Intent intent = new Intent(MainActivity.this, CitasActivity.class);
                            intent.putExtra("id_paciente",""+id_paciente);
                            startActivity(intent);
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Usuario/Contraseña incorrecta(s)")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                        }
                                    });
                            final AlertDialog dialog = builder.create();
                            dialog.show();
                        }
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
                params.put("usuario", usuario);
                params.put("pass", pass);


                return params;
            }

        };

        mRequestQueue.add(putRequest);
    }


}

