package uca.es.test;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Calendar;


public class Paciente_EditarActivity extends AppCompatActivity {

    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //Widgets
    private TextView tnombre;
    private TextView tapellidos;
    private TextView tfecha;
    private TextView ttelefono;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente__editar);

        Toolbar barra = (Toolbar) findViewById(R.id.my_toolbar);
        barra.setTitle("Ficha personal: "+getIntent().getStringExtra("nombre")+" "+getIntent().getStringExtra("apellidos"));
        setSupportActionBar(barra);
        Button editar = (Button) findViewById(R.id.editar);
        Button binformes = (Button) findViewById(R.id.bInformes);
        Button eliminar = (Button) findViewById(R.id.eliminar);
        Button banamnesis = (Button) findViewById(R.id.banamnesis);
        Button bestructura1 = (Button) findViewById(R.id.bestructura1);
        Button bestructura2 = (Button) findViewById(R.id.bestructura2);
        Button bVisceral = (Button) findViewById(R.id.bVisceral);
        Button brespiratorio = (Button) findViewById(R.id.brespiratorio);
        Button bcraneo = (Button) findViewById(R.id.bcraneo);
        Button btratamiento = (Button) findViewById(R.id.btratamiento);
        Button bdescarga = (Button) findViewById(R.id.bdescarga);
        Button beliminarcuestionario = (Button) findViewById(R.id.beliminarc);

        tnombre = (TextView) findViewById(R.id.tNombre_paciente);
        tapellidos = (TextView) findViewById(R.id.tApellido_paciente);
        ttelefono = (TextView) findViewById(R.id.tTelefono_paciente);
        tfecha = (TextView) findViewById(R.id.tFecha_paciente);


        ttelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(Paciente_EditarActivity.this);
                builder.setMessage("¿Desea llamar al paciente?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                if (Build.VERSION.SDK_INT > 22) {

                                    if (ActivityCompat.checkSelfPermission(Paciente_EditarActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                        ActivityCompat.requestPermissions(Paciente_EditarActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

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


        //rellenar campos con la información del paciente seleccionado

        tnombre.setText(tnombre.getText()+getIntent().getStringExtra("nombre"));
        tapellidos.setText(tapellidos.getText()+getIntent().getStringExtra("apellidos"));
        tfecha.setText(tfecha.getText()+getIntent().getStringExtra("fecha_nacimiento"));
        ttelefono.setText(ttelefono.getText()+getIntent().getStringExtra("telefono"));


        //--------------------------------------------------------------

        mRequestQueue = Volley.newRequestQueue(this);

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Paciente_EditarActivity.this, PacienteEditar2Activity.class);
                intent.putExtra("id",getIntent().getStringExtra("id"));
                intent.putExtra("nombre",tnombre.getText().toString());
                intent.putExtra("apellidos",tapellidos.getText().toString());
                intent.putExtra("fecha_nacimiento",tfecha.getText().toString());
                intent.putExtra("telefono",ttelefono.getText().toString());
                startActivityForResult(intent,1);

            }
        });

        binformes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Paciente_EditarActivity.this, InformeActivity.class);
                //enviamos el id del paciente, para guardar su información
                intent.putExtra("id_paciente",getIntent().getStringExtra("id"));
                intent.putExtra("nombre",getIntent().getStringExtra("nombre"));
                intent.putExtra("apellidos",getIntent().getStringExtra("apellidos"));
                startActivity(intent);

            }
        });

        bdescarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(Paciente_EditarActivity.this);
                builder.setMessage("¿Desea crear o sobrescribir un informe con la información del paciente de hoy?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                descargar_informacion();
                                Toast toast1 =
                                        Toast.makeText(getApplicationContext(),
                                                "Informe generado correctamente", Toast.LENGTH_SHORT);

                                toast1.show();

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

        banamnesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Paciente_EditarActivity.this, AnamnesisActivity.class);
                //enviamos el id del paciente, para guardar su información
                intent.putExtra("id_paciente",getIntent().getStringExtra("id"));
                intent.putExtra("nombre",getIntent().getStringExtra("nombre"));
                intent.putExtra("apellidos",getIntent().getStringExtra("apellidos"));
                startActivity(intent);
            }
        });

        btratamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Paciente_EditarActivity.this, TratamientoActivity.class);
                //enviamos el id del paciente, para guardar su información
                intent.putExtra("id_paciente",getIntent().getStringExtra("id"));
                intent.putExtra("nombre",getIntent().getStringExtra("nombre"));
                intent.putExtra("apellidos",getIntent().getStringExtra("apellidos"));
                startActivity(intent);
            }
        });

        bestructura1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Paciente_EditarActivity.this, EstructuraActivity.class);
                //enviamos el id del paciente, para guardar su información
                intent.putExtra("id_paciente",getIntent().getStringExtra("id"));
                intent.putExtra("nombre",getIntent().getStringExtra("nombre"));
                intent.putExtra("apellidos",getIntent().getStringExtra("apellidos"));
                startActivity(intent);
            }
        });

        bestructura2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Paciente_EditarActivity.this, Estructura2Activity.class);
                //enviamos el id del paciente, para guardar su información
                intent.putExtra("id_paciente",getIntent().getStringExtra("id"));
                intent.putExtra("nombre",getIntent().getStringExtra("nombre"));
                intent.putExtra("apellidos",getIntent().getStringExtra("apellidos"));
                startActivity(intent);
            }
        });

        bVisceral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Paciente_EditarActivity.this, VisceralActivity.class);
                //enviamos el id del paciente, para guardar su información
                intent.putExtra("id_paciente",getIntent().getStringExtra("id"));
                intent.putExtra("nombre",getIntent().getStringExtra("nombre"));
                intent.putExtra("apellidos",getIntent().getStringExtra("apellidos"));
                startActivity(intent);
            }
        });

        brespiratorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Paciente_EditarActivity.this, RespiratorioActivity.class);
                //enviamos el id del paciente, para guardar su información
                intent.putExtra("id_paciente",getIntent().getStringExtra("id"));
                intent.putExtra("nombre",getIntent().getStringExtra("nombre"));
                intent.putExtra("apellidos",getIntent().getStringExtra("apellidos"));
                startActivity(intent);
            }
        });

        bcraneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Paciente_EditarActivity.this, CraneoActivity.class);
                //enviamos el id del paciente, para guardar su información
                intent.putExtra("id_paciente",getIntent().getStringExtra("id"));
                intent.putExtra("nombre",getIntent().getStringExtra("nombre"));
                intent.putExtra("apellidos",getIntent().getStringExtra("apellidos"));
                startActivity(intent);
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Paciente_EditarActivity.this);
                builder.setMessage("¿Está seguro que quiere eliminar el paciente?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                eliminar_paciente();
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

        beliminarcuestionario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Paciente_EditarActivity.this);
                builder.setMessage("¿Está seguro que quiere eliminar las respuestas del cuestionario de síntomas sin crear antes un informe?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                eliminar_repuestas();
                                Toast toast1 =
                                        Toast.makeText(getApplicationContext(),
                                                "Respuestas del cuestionario eliminadas", Toast.LENGTH_SHORT);

                                toast1.show();

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

    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == 1){
            if(resultCode == -1) {
                tnombre.setText(data.getStringExtra("nombre"));
                tapellidos.setText(data.getStringExtra("apellidos"));
                tfecha.setText(data.getStringExtra("fecha_nacimiento"));
                ttelefono.setText(data.getStringExtra("telefono"));
            }
        }
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

    private void eliminar_repuestas() {
        String url = "https://playasconil.es/tfg/eliminar_respuesta_cuestionario.php?id_paciente="+getIntent().getStringExtra("id");

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

    private void eliminar_paciente() {
        String url = "https://playasconil.es/tfg/eliminar_paciente.php?id="+getIntent().getStringExtra("id");

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

    private void descargar_informacion(){
        String nombre_archivo = getIntent().getStringExtra("nombre").replace("  "," ").replace(" ","_")+"_"+getIntent().getStringExtra("apellidos").replace("  "," ").replace(" ","_");
        String url = "https://playasconil.es/tfg/crear_informe.php?id="+getIntent().getStringExtra("id")+"&nombre="+nombre_archivo;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("respuesta","Informe creado");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);

    }


}
