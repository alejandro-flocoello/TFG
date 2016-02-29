package es.upm.ssr.gatv.tfg;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Oh, este botón no tiene ninguna función asignada.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the Send button */
    public void muestraVideos(View view) {
        // Do something in response to button Videos
        Intent intent = new Intent(this, VideosActivity.class);
        startActivity(intent);
    }

    public void muestraMensajes(View view) {
        // Do something in response to button Mensajes
        Intent intent = new Intent(this, MensajesActivity.class);
        startActivity(intent);
    }

    public void muestraAlarma(View view) {
        // Do something in response to button Alarma
        Intent intent = new Intent(this, AlarmaActivity.class);
        startActivity(intent);
    }
    public void muestraJuegos(View view) {
        // Do something in response to button Juegos
        Intent intent = new Intent(this, JuegosActivity.class);
        startActivity(intent);
    }

    public void muestraVideoConferencia(View view) {
        // Do something in response to button VideoConferencia
        Intent intent = new Intent(this, VideoConferenciaActivity.class);
        startActivity(intent);
    }

    public void muestraAjustes(View view) {
        // Do something in response to button Ajustes
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


}
