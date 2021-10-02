package uca.es.test;

import android.app.DatePickerDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class PersonalActivity extends AppCompatActivity {

    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //Widgets
    private EditText efnac;
    private EditText enombre;
    private EditText eapellido;
    private EditText etelefono;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        efnac = (EditText) findViewById(R.id.efnac);
        enombre = (EditText) findViewById(R.id.enombre);
        eapellido = (EditText) findViewById(R.id.eapellido);
        etelefono = (EditText) findViewById(R.id.etelefono);
        Button ibObtenerFecha = (Button) findViewById(R.id.bfnac);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbarp);
        barra.setTitle("Ficha Personal");
        ibObtenerFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }
        });
        Button guardar = (Button) findViewById(R.id.editar);
        Button citas = (Button) findViewById(R.id.bInformes);

        mRequestQueue = Volley.newRequestQueue(this);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paciente p = new Paciente(enombre.getText().toString(),eapellido.getText().toString(),efnac.getText().toString(),etelefono.getText().toString());
                Toast.makeText(getApplicationContext(),
                        "PACIENTE INTRDOCIDO", Toast.LENGTH_LONG);
                introducir_paciente(p);
            }
        });

        citas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



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
                efnac.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }


    public void introducir_paciente(final Paciente p) {
        final String nombre = p.getNombre();
        final String apellidos= p.getApellidos();
        final String f_nac = p.getFecha_nacimiento();
        final String telefono = p.getTelefono();
        String url = "https://playasconil.es/tfg/insertar_paciente.php";
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
                params.put("nombre", nombre);
                params.put("apellidos", apellidos);
                params.put("f_nac", f_nac);
                params.put("telefono", telefono);

                return params;
            }
        };

        mRequestQueue.add(postRequest);
    }
}
