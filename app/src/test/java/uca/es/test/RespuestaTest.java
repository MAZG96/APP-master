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
public class RespuestaTest {
    RequestQueue mRequestQueue;

    @Before
    public void setUP() {
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    final Respuesta r = new Respuesta(100,9,54,"Ardor");

    //TEST DE INSERCION DE LA RESPUESTA EN LA BD
    @Test
    public void insertar_respuesta() {

        String url = "https://playasconil.es/tfg/insertar_respuesta.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        //Comprobamos que insertar_respuesta.php nos devuelve que la inserccion se ha realizado
                        assertEquals("respuesta insertada",response);
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
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("id",String.valueOf(r.getNumber()));
                params.put("id_pregunta", String.valueOf(r.getId_pregunta()));
                params.put("id_paciente", String.valueOf(r.getId_paciente()));
                params.put("respuesta", r.getTexto());

                return params;
            }

        };

        mRequestQueue.add(postRequest);
    }

    //TEST DE OBTENER RESPUESTA DE LA BD
    @Test
    public void get_respuesta() {

        String url = "https://playasconil.es/tfg/getRespuesta.php?id_pregunta="+r.getId_pregunta()+"&id_paciente="+r.getId_paciente();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("respuesta");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String id_paciente = hit.getString("id_paciente");
                                String id_pregunta = hit.getString("id_pregunta");
                                String respuesta = hit.getString("texto");

                                //Comprobamos que la respuesta que obtenemos, es el mismo insertado previamente
                                assertEquals(id_paciente,"54");
                                assertEquals(id_pregunta,"9");
                                assertEquals(respuesta,"Ardor");

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

    //TEST DE MODIFICACIÓN DE LA RESPUESTA EN LA BD
    @Test
    public void modificar_respuesta() {
        String url = "https://playasconil.es/tfg/actualizar_respuesta.php";
        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
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
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("id_pregunta", String.valueOf(r.getId_pregunta()));
                params.put("id_paciente", String.valueOf(r.getId_paciente()));
                params.put("respuesta", "Gases");

                return params;
            }

        };

        mRequestQueue.add(putRequest);



        //Obtenemos la misma respuesta de la BD para comprobar que se ha modificado

        url = "https://playasconil.es/tfg/getRespuesta.php?id_pregunta="+r.getId_pregunta()+"&id_paciente="+r.getId_paciente();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("respuesta");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String id_paciente = hit.getString("id_paciente");
                                String id_pregunta = hit.getString("id_pregunta");
                                String respuesta = hit.getString("texto");

                                //Comprobamos que la respuesta que obtenemos ha sido modificado
                                assertEquals(id_paciente,"54");
                                assertEquals(id_pregunta,"9");
                                assertEquals(respuesta,"Gases");  // modificado de Ardor

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

    //TEST DE ELIMINACIÓN DE LA RESPUESTA EN LA BD
    @Test
    public void eliminar_respuesta() {
        String url = "https://playasconil.es/tfg/eliminar_respuesta.php?id_pregunta="+r.getId_pregunta()+"&id_paciente="+r.getId_paciente();

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
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
                });

        mRequestQueue.add(deleteRequest);


        //COMPROBAR QUE LA RESPUESTA NO EXISTE
        url = "https://playasconil.es/tfg/getRespuesta.php?id_pregunta="+r.getId_pregunta()+"&id_paciente="+r.getId_paciente();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("respuesta");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                //Comprobamos que la respuesta no existe
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
