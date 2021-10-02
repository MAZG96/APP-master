package uca.es.test;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CitasActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //variables para el calendario

    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //----------------------------------------------------
    private int alarmID = 2;
    RequestQueue mRequestQueue;
    ArrayList<Cita> citas = new ArrayList<>();
    static ArrayList<String> citas_fecha = new ArrayList<>();
    static String id_usuario = "0";
    private EditText eBuscar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);
        id_usuario = "0";
        //TITULO TOOLBAR
        Toolbar barra = (Toolbar) findViewById(R.id.toolbarp);
        barra.setTitle("Citas");
        eBuscar = (EditText) findViewById(R.id.eBuscar);
        Button bbuscar = (Button) findViewById(R.id.bbuscar);
        FloatingActionButton bAdd = (FloatingActionButton) findViewById(R.id.bAdd);
        setSupportActionBar(barra);
        // Referenciamos al RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
// Mejoramos rendimiento con esta configuración
        mRecyclerView.setHasFixedSize(true);
// Creamos un LinearLayoutManager para gestionar el item.xml creado antes
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
// Lo asociamos al RecyclerView
        mRecyclerView.setLayoutManager(mLayoutManager);
// Creamos un ArrayList de preguntas



        if(getIntent().getStringExtra("id_paciente") != null) {
            bAdd.setVisibility(View.INVISIBLE);
            id_usuario = getIntent().getStringExtra("id_paciente");
        }
        mRequestQueue = Volley.newRequestQueue(this);
        mostrar_citas();


// JSON PARSE STRING



        bbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CitasActivity.this, NuevaCitaActivity.class);
                startActivity(intent);
            }
        });


        final CitasAdapter citasAdapter = new CitasAdapter(citas,id_usuario);
        mRecyclerView.setAdapter(citasAdapter);

        eBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mRecyclerView.setAdapter(citasAdapter);
                citasAdapter.getFilter().filter(s.toString());

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    public void mostrar_citas() {
        citas_fecha.clear();
        String url = "https://playasconil.es/tfg/mostrar_citas.php";
        if(getIntent().getStringExtra("id_paciente") != null) {
            url = "https://playasconil.es/tfg/mostrar_citas.php?id_paciente="+getIntent().getStringExtra("id_paciente");
        }

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

                                if(getIntent().getStringExtra("id_paciente") != null) {
                                    Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH).parse(fecha);
                                    long milliseconds = date.getTime();
                                    if (!date.before(new Date())) { // si la fecha aún no ha pasado se activa en las alarmas
                                        long margen_aviso = 3600000; //1 hora = 3600000 milisegundos
                                        Utils.setAlarm(alarmID, milliseconds - margen_aviso, CitasActivity.this);
                                    }
                                }

                                String val = hit.getString("valoracion");
                                citas_fecha.add(fecha);
                                citas.add(new Cita(number,id_paciente,fecha,val));
                            }
                            mostrar_nombre_citas(citas);




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

    public void mostrar_nombre_citas(final ArrayList<Cita> copia_citas){

        String url = "https://playasconil.es/tfg/mostrar_pacientes.php";
        final ArrayList<Respuesta> respondidas = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("paciente");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                int id_paciente = hit.getInt("id");
                                for(int k=0;k<copia_citas.size();k++) {
                                    if (id_paciente == copia_citas.get(k).getId_paciente()){
                                        copia_citas.get(k).setNombre_paciente(
                                                hit.getString("nombre")+" "+hit.getString("apellidos"));
                                    }

                                }

                            }

                            mAdapter = new CitasAdapter(copia_citas,id_usuario);
                            mRecyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {


                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { ;
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);

    }







    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "DatePickerDialog");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        List<Calendar> dates = new ArrayList<>();
        for(int i=0;i<citas_fecha.size();i++) {
            java.util.Date date = null;

            try {
                date = sdf.parse(citas_fecha.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar = dateToCalendar(date);



            dates.add(calendar);
        }
        Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
        dpd.setSelectableDays(disabledDays1);


    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }




    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        final int mesActual = monthOfYear + 1;
        //Formateo el día obtenido: antepone el 0 si son menores de 10
        String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
        //Formateo el mes obtenido: antepone el 0 si son menores de 10
        String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
        //Muestro la fecha con el formato deseado
        eBuscar.setText(diaFormateado+"-"+mesFormateado+"-"+year);



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
                citas.clear();
                mostrar_citas();


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}