package uca.es.test;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class NuevaCita2Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

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

    RequestQueue mRequestQueue;
    ArrayList<Cita> citas = new ArrayList<>();
    ArrayList<Cita> citas_ocupadas = new ArrayList<>();
    ArrayList<Cita> horas = new ArrayList<>();
    static ArrayList<String> citas_fecha = new ArrayList<>();
    private EditText eBuscar;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cita2);
        mRequestQueue = Volley.newRequestQueue(this);
        //TITULO TOOLBAR
        Toolbar barra = (Toolbar) findViewById(R.id.toolbarp);
        barra.setTitle("Elige fecha: pulsa buscar");
        setSupportActionBar(barra);

        eBuscar = (EditText) findViewById(R.id.eBuscar);
        Button bbuscar = (Button) findViewById(R.id.bbuscar);


        String id_cita = getIntent().getStringExtra("id_cita");
        if(id_cita != null) {
            setSupportActionBar(barra);
        }
        // Referenciamos al RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
// Mejoramos rendimiento con esta configuración
        mRecyclerView.setHasFixedSize(true);
// Creamos un LinearLayoutManager para gestionar el item.xml creado antes
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
// Lo asociamos al RecyclerView
        mRecyclerView.setLayoutManager(mLayoutManager);
// Creamos un ArrayList de preguntas

// JSON PARSE STRING

        final NuevaCita2Adapter ncAdapter = new NuevaCita2Adapter(horas,getIntent().getIntExtra("id_paciente",0),id_cita);
        bbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });


        eBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                buscar_cita(s.toString());

                mRecyclerView.setAdapter(ncAdapter);




            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    public void buscar_cita(final String fecha) {
        String url = "https://playasconil.es/tfg/getCitas.php?fecha="+fecha;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("cita");



                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String fecha= hit.getString("fecha");
                                int number = hit.getInt("id");
                                int id_paciente = hit.getInt("id_paciente");

                                Cita c = new Cita(fecha,number,id_paciente);
                                citas_ocupadas.add(c);
                            }
                            horas.clear();
                            horas.add(new Cita(fecha+" 9:00:00",0,0));
                            horas.add(new Cita(fecha+" 10:00:00",0,0));
                            horas.add(new Cita(fecha+" 11:00:00",0,0));
                            horas.add(new Cita(fecha+" 12:00:00",0,0));
                            horas.add(new Cita(fecha+" 13:00:00",0,0));
                            horas.add(new Cita(fecha+" 16:00:00",0,0));
                            horas.add(new Cita(fecha+" 17:00:00",0,0));
                            horas.add(new Cita(fecha+" 18:00:00",0,0));
                            if(!citas_ocupadas.isEmpty()){
                                for(int i=0;i<citas_ocupadas.size();i++){
                                    for(int j=0;j<horas.size();j++){
                                        if(horas.get(j).getFecha().compareTo(citas_ocupadas.get(i).getFecha())==0){

                                            System.out.println("HORAS "+horas.get(j).getFecha());
                                            System.out.println("ocupadas "+citas_ocupadas.get(i).getFecha());
                                            horas.get(j).setId_paciente(getIntent().getIntExtra("id_paciente",0));
                                        }
                                    }
                                }
                            }
                            NuevaCita2Adapter citasAdapt = new NuevaCita2Adapter(horas,getIntent().getIntExtra("id_paciente",0),getIntent().getStringExtra("id_cita"));
                            mRecyclerView.setAdapter(citasAdapt);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                horas.clear();
                horas.add(new Cita(fecha+" 9:00:00",0,0));
                horas.add(new Cita(fecha+" 10:00:00",0,0));
                horas.add(new Cita(fecha+" 11:00:00",0,0));
                horas.add(new Cita(fecha+" 12:00:00",0,0));
                horas.add(new Cita(fecha+" 13:00:00",0,0));
                horas.add(new Cita(fecha+" 16:00:00",0,0));
                horas.add(new Cita(fecha+" 17:00:00",0,0));
                horas.add(new Cita(fecha+" 18:00:00",0,0));
                NuevaCita2Adapter citasAdapt = new NuevaCita2Adapter(horas,getIntent().getIntExtra("id_paciente",0),getIntent().getStringExtra("id_cita"));
                mRecyclerView.setAdapter(citasAdapt);
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