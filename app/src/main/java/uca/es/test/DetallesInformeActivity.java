package uca.es.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class DetallesInformeActivity extends AppCompatActivity {

    private WebView tInforme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_informe);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbarp);
        barra.setTitle("Informe: "+getIntent().getStringExtra("nombre_archivo"));
        setSupportActionBar(barra);

        tInforme = (WebView) findViewById(R.id.tInforme);

        tInforme.loadUrl(getIntent().getStringExtra("direccion"));




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
                tInforme.loadUrl(getIntent().getStringExtra("direccion"));

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

