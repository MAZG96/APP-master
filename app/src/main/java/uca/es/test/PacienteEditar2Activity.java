package uca.es.test;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class PacienteEditar2Activity extends AppCompatActivity {

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
    private EditText econtra;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_editar2);

        efnac = (EditText) findViewById(R.id.efnac);
        enombre = (EditText) findViewById(R.id.enombre);
        eapellido = (EditText) findViewById(R.id.eapellido);
        etelefono = (EditText) findViewById(R.id.etelefono);
        econtra = (EditText) findViewById(R.id.econtra);
        Button ibObtenerFecha = (Button) findViewById(R.id.bfnac);
        Button editar = (Button) findViewById(R.id.editar);
        Toolbar barra = (Toolbar) findViewById(R.id.my_toolbar);
        barra.setTitle("Editar Paciente: "+getIntent().getStringExtra("nombre")+" "+getIntent().getStringExtra("apellidos"));
        setSupportActionBar(barra);

        enombre.setText(getIntent().getStringExtra("nombre"));
        eapellido.setText(getIntent().getStringExtra("apellidos"));
        efnac.setText(getIntent().getStringExtra("fecha_nacimiento"));
        etelefono.setText(getIntent().getStringExtra("telefono"));

        ibObtenerFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }
        });

        mRequestQueue = Volley.newRequestQueue(this);

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!enombre.getText().toString().isEmpty() && !eapellido.getText().toString().isEmpty() && !efnac.getText().toString().isEmpty()
                        && !(etelefono.getText().toString().length()!=9) && (econtra.getText().toString().isEmpty() || econtra.getText().toString().length()>=8)) {
                    Paciente p = new Paciente(enombre.getText().toString(), eapellido.getText().toString(), efnac.getText().toString(), etelefono.getText().toString());
                    String contra = econtra.getText().toString();
                    editar_paciente(p, contra);

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(PacienteEditar2Activity.this);
                    builder.setMessage("Complete todos los campos (contraseña opcional) para editar al paciente")
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

    public void editar_paciente(final Paciente p,final String contra) {
        final String id = getIntent().getStringExtra("id");
        final String nombre = p.getNombre();
        final String apellidos= p.getApellidos();
        final String f_nac = p.getFecha_nacimiento();
        final String telefono = p.getTelefono();
        String url = "https://playasconil.es/tfg/modificar_paciente.php";
        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        if(response.equals("telefono duplicado")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(PacienteEditar2Activity.this);
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

                            Toast toast1 =
                                    Toast.makeText(getApplicationContext(),
                                            "PACIENTE EDITADO", Toast.LENGTH_SHORT);

                            toast1.show();

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("nombre", enombre.getText().toString());
                            resultIntent.putExtra("apellidos", eapellido.getText().toString());
                            resultIntent.putExtra("fecha_nacimiento", efnac.getText().toString());
                            resultIntent.putExtra("telefono", etelefono.getText().toString());

                            setResult(PacienteEditar2Activity.RESULT_OK, resultIntent);
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
                params.put("id", id);
                params.put("nombre", nombre);
                params.put("apellidos", apellidos);
                params.put("f_nac", f_nac);
                params.put("telefono", telefono);
                params.put("contra",contra);

                return params;
            }

        };

        mRequestQueue.add(putRequest);
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