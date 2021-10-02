package uca.es.test;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;


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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@Config(manifest= Config.NONE)
public class PacientesTest{

    RequestQueue mRequestQueue;

    @Before
    public void setUP() {
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    final Paciente p = new Paciente(53, "Miguel", "Zara", "15/01/1996", "633907619","contra");

    //TEST DE INSERCION DEL PACIENTE EN LA BD
    @Test
    public void insertar_paciente() {

        String url = "https://playasconil.es/tfg/insertar_paciente.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        //Comprobamos que insertar_paciente.php nos devuelve que la inserccion se ha realizado
                        assertEquals("paciente insertado",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nombre", p.getNombre());
                params.put("apellidos", p.getApellidos());
                params.put("f_nac", p.getFecha_nacimiento());
                params.put("telefono", p.getTelefono());


                return params;
            }

        };

        mRequestQueue.add(postRequest);
    }

    //TEST DE OBTENER PACIENTE DE LA BD
    @Test
    public void get_paciente() {
        final String[] nombre = new String[1];
        final String[] apellidos = new String[1];
        final String[] telefono = new String[1];
        final String[] fecha_nacimiento = new String[1];
        final String[] pass = new String[1];



        String url = "https://playasconil.es/tfg/mostrar_paciente.php?id_paciente=" + p.getId();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("paciente");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                //Obtenemos los datos del JSON que nos devuelve la petición
                                int id = hit.getInt("id");
                                String nombre = hit.getString("nombre");
                                String apellidos = hit.getString("apellidos");
                                String fecha_nacimiento = hit.getString("fecha_nacimiento");
                                String telefono = hit.getString("telefono");
                                String pass = hit.getString("pass");

                                //Comprobamos que el paciente que obtenemos, es el mismo insertado previamente
                                assertEquals(nombre,"Miguel");
                                assertEquals(apellidos,"Zara");
                                assertEquals(fecha_nacimiento,"15/01/1996");
                                assertEquals(telefono,"633907618");
                                assertEquals(pass,"contra");

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


        mRequestQueue.add(getRequest);
    }

    //TEST DE MODIFICACIÓN DEL PACIENTE EN LA BD
    @Test
    public void modificar_paciente() {
        String url = "https://playasconil.es/tfg/modificar_paciente.php?id=" + p.getId();
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        //comprobamos que la petición nos devuelve el mensaje de que el paciente fue modificado
                        assertEquals("paciente modificado",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nombre", "MiguelModificado");
                params.put("apellidos", p.getApellidos());
                params.put("f_nac", p.getFecha_nacimiento());
                params.put("telefono", p.getTelefono());


                return params;
            }

        };

        mRequestQueue.add(putRequest);


        //Obtenemos el mismo paciente de la BD para comprobar que se ha modificado

        url = "https://playasconil.es/tfg/mostrar_paciente.php?id_paciente=" + p.getId();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("paciente");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                int id = hit.getInt("id");
                                String nombre = hit.getString("nombre");


                                //Comprobamos que el paciente ha sido modificado
                                assertEquals("MiguelModificado", nombre);

                            }

                            eliminar_paciente();
                            System.out.println("HELLOOOOOO");

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
        mRequestQueue.add(getRequest);
    }

    //TEST DE ELIMINACIÓN DEL PACIENTE EN LA BD
    @Test
    public void eliminar_paciente() {
        String url = "https://playasconil.es/tfg/eliminar_paciente.php?id_paciente=" + p.getId();

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        //Comprobamos que la peticion devuelve el mensaje de que el paciente fue eliminado
                        assertEquals("paciente eliminado",response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                });

        mRequestQueue.add(deleteRequest);
        setUP();


        //COMPROBAR QUE EL PACIENTE NO EXISTA EN LA BD

        url = "https://playasconil.es/tfg/mostrar_paciente.php?id_paciente=" + p.getId();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("paciente");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                //Comprobamos que el paciente no existe
                                assertTrue(hit.isNull("id"));


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
        mRequestQueue.add(getRequest);

    }
}
