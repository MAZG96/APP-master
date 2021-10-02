package uca.es.test;

import android.app.DatePickerDialog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NuevoPacienteActivity extends AppCompatActivity {

    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    private RequestQueue mRequestQueue;
    private TextView enombre;
    private TextView eapellidos;
    private TextView efecha;
    private TextView etelefono;
    private TextView epass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_paciente);

        Toolbar barra = (Toolbar) findViewById(R.id.my_toolbar);
        barra.setTitle("Nuevo Paciente");
        setSupportActionBar(barra);
        Button addPaciente = (Button) findViewById(R.id.addPaciente);

        enombre = (TextView) findViewById(R.id.enombre);
        eapellidos = (TextView) findViewById(R.id.eapellido);
        efecha = (TextView) findViewById(R.id.efnac);
        etelefono = (TextView) findViewById(R.id.etelefono);
        Button ibObtenerFecha = (Button) findViewById(R.id.bfnac);
        epass = (TextView) findViewById(R.id.epass);

        mRequestQueue = Volley.newRequestQueue(this);

        //al pulsar el botón aparece el calendario por pantalla
        ibObtenerFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }
        });

        //boton para añadir paciente
        addPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 final String pass = String.valueOf(epass.getText());
                 //Comprobación de campos vacíos
                if(!enombre.getText().toString().isEmpty() && !eapellidos.getText().toString().isEmpty() && !efecha.getText().toString().isEmpty()
                 && !(etelefono.getText().toString().length()!=9) && !(epass.getText().toString().length()<8)){

                    Paciente p = new Paciente(enombre.getText().toString(),eapellidos.getText().toString(),efecha.getText().toString(),etelefono.getText().toString());
                    insertar_paciente(p,pass);



                }else{
                    //Lanza mensaje si hay algún campo vacío
                    AlertDialog.Builder builder = new AlertDialog.Builder(NuevoPacienteActivity.this);
                    builder.setMessage("Complete todos los campos para registrar al paciente")
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
        });

    }

    public void insertar_paciente(final Paciente p, final String pass) {


        final String nombre = p.getNombre();
        final String apellidos= p.getApellidos();
        final String f_nac = p.getFecha_nacimiento();
        final String telefono = p.getTelefono();
        //hace la petición POST al webservice e introduce el paciente a la base de datos
        String url = "https://playasconil.es/tfg/insertar_paciente.php";
        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //si existe un paciente con el mismo número, no permite introducir el nuevo apciente
                        if(response.equals("telefono duplicado")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(NuevoPacienteActivity.this);
                            builder.setMessage("Exite un usuario con este número de teléfono registrado")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                        }
                                    });
                            final AlertDialog dialog = builder.create();
                            dialog.show();
                        }else{
                            //muestra mensaje en pantalla de paciente registrado
                            Toast toast1 =
                                    Toast.makeText(getApplicationContext(),
                                            "PACIENTE NUEVO REGISTRADO", Toast.LENGTH_SHORT);

                            toast1.show();
                            finish();
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
                params.put("nombre", nombre);
                params.put("apellidos", apellidos);
                params.put("f_nac", f_nac);
                params.put("telefono", telefono);
                params.put("pass",pass);

                return params;
            }

        };

        mRequestQueue.add(putRequest);
    }

    private void obtenerFecha() {
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                efecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
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