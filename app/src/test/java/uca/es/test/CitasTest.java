package uca.es.test;

import android.util.Log;

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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@Config(manifest= Config.NONE)
public class CitasTest {
    RequestQueue mRequestQueue;

    @Before
    public void setUP() {
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    final Cita c = new Cita(100,53,"26/08/2021 16:00:00","3.5");

    //TEST DE INSERCION DE LA CITA EN LA BD
    @Test
    public void insertar_cita() {
        final String fecha = c.getFecha();
        final String id_paciente = Integer.toString(c.getId_paciente());
        String url = "https://playasconil.es/tfg/insertar_cita.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        //Comprobamos que insertar_cita.php nos devuelve que la inserccion se ha realizado
                        assertEquals("cita insertada",response);


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
                params.put("fecha", fecha);
                params.put("id_paciente", id_paciente);


                return params;
            }

        };

        mRequestQueue.add(postRequest);
    }

    //TEST DE OBTENER CITA DE LA BD
    @Test
    public void get_cita() {
        String url = "https://playasconil.es/tfg/mostrar_citas.php?id_paciente=" + c.getId_paciente();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("cita");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                int id = hit.getInt("id");
                                String fecha = hit.getString("fecha");
                                String id_paciente = hit.getString("id_paciente");
                                String valoracion = hit.getString("valoracion");

                                //Comprobamos que la cita obtenida es la misma que insertamos
                                assertEquals(id,100);
                                assertEquals(fecha,"26/08/2021 16:00:00");
                                assertEquals(id_paciente,"53");
                                assertEquals(valoracion,"3.5");

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

    //TEST DE MODIFICACIÓN DE LA CITA EN LA BD
    @Test
    public void modificar_cita() {
        String url = "https://playasconil.es/tfg/modificar_cita.php?id=" + c.getNumber();
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
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
                params.put("fecha", "26/08/2021 17:00:00");
                return params;
            }

        };

        mRequestQueue.add(putRequest);



        //Obtenemos la misma cita de la BD para comprobar que se ha modificado

        url = "https://playasconil.es/tfg/mostrar_citas.php?id_paciente=" + c.getId_paciente();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("cita");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                int id = hit.getInt("id");
                                String fecha = hit.getString("fecha");

                                //Comprobamos que la cita obtenida es la misma que insertamos
                                assertEquals(fecha,"26/08/2021 17:00:00"); //cambiamos la hora a las 17:00:00
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

    //TEST DE ELIMINACIÓN DE LA CITA EN LA BD
    @Test
    public void eliminar_cita() {
        String url = "https://playasconil.es/tfg/eliminar_cita.php?id=" + c.getNumber();

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        assertEquals(response,"citas eliminada");
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

        //COMPROBAR QUE LA CITA NO EXISTA EN LA BD
        url = "https://playasconil.es/tfg/mostrar_cita.php?id_citae=" + c.getNumber();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("cita");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                //Comprobamos que la cita ya no existe
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
