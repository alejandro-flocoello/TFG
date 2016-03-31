package es.upm.ssr.gatv.tfg;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Drawable d = getResources().getDrawable(R.drawable.border_icon_2);
        final Drawable u = getResources().getDrawable(R.drawable.border_icon_4);

        final TextView juegos_text = (TextView) findViewById(R.id.textMenuJuegos);
        final TextView videos_text = (TextView) findViewById(R.id.textMenuVideos);
        final TextView videocnf_text = (TextView) findViewById(R.id.textMenuVideoConf);
        final TextView alarma_text = (TextView) findViewById(R.id.textMenuAlarma);
        final TextView mensajes_text = (TextView) findViewById(R.id.textMenuMensajes);
        final TextView ajustes_text = (TextView) findViewById(R.id.textMenuSettings);



        final ImageButton juegos_btn = (ImageButton) findViewById(R.id.buttonJuegos);
        final ImageButton ajustes_btn = (ImageButton) findViewById(R.id.buttonAjustes);
        final ImageButton videocnf_btn = (ImageButton) findViewById(R.id.buttonVideoconferencia);
        final ImageButton videos_btn = (ImageButton) findViewById(R.id.buttonVideos);
        final ImageButton mensajes_btn = (ImageButton) findViewById(R.id.buttonMensajes);
        final ImageButton alarma_btn = (ImageButton) findViewById(R.id.buttonAlarma);



        juegos_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                        juegos_btn.setBackground(u);
                        juegos_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                }else if(event.getAction() == KeyEvent.ACTION_UP){
                        juegos_btn.setBackground(d);
                        juegos_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);

                }

            return false;}
        });

        ajustes_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    ajustes_btn.setBackground(u);
                    ajustes_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                }else if(event.getAction() == KeyEvent.ACTION_UP){
                    ajustes_btn.setBackground(d);
                    ajustes_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                }

                return false;}
        });

        videocnf_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    videocnf_btn.setBackground(u);
                    videocnf_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                }else if(event.getAction() == KeyEvent.ACTION_UP){
                    videocnf_btn.setBackground(d);
                    videocnf_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                }

                return false;}
        });

        videos_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    videos_btn.setBackground(u);
                    videos_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    videos_btn.setBackground(d);
                    videos_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                }

                return false;
            }
        });

        mensajes_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    mensajes_btn.setBackground(u);
                    mensajes_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                }else if(event.getAction() == KeyEvent.ACTION_UP){
                    mensajes_btn.setBackground(d);
                    mensajes_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                }

                return false;}
        });

        alarma_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    alarma_btn.setBackground(u);
                    alarma_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,28);
                }else if(event.getAction() == KeyEvent.ACTION_UP){
                    alarma_btn.setBackground(d);
                    alarma_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                }

                return false;}
        });


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
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);

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
