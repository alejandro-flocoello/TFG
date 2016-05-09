package es.upm.ssr.gatv.tfg;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity  extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final int RESULT_SETTINGS = 1;
    private Typeface font_face;
    private TextToSpeech tts;
    private final Locale SPANISH = new Locale("es","ES");
    private Boolean tts_enabled = false;
    private SharedPreferences sharedPrefs;
//    public ReceiverService s;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts = new TextToSpeech(this,this);
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

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Integer font_value = Integer.parseInt(sharedPrefs.getString("set_text_font_list", "0"));

        switch (font_value) {
            case 0:
                font_face = Typeface.createFromAsset(getAssets(), "fonts/Arial.ttf");
                break;
            case 1:
                font_face = Typeface.createFromAsset(getAssets(), "fonts/Helvetica.ttf");
                break;
            case 2:
                font_face = Typeface.createFromAsset(getAssets(), "fonts/Tahoma.ttf");
                break;
            case 3:
                font_face = Typeface.createFromAsset(getAssets(), "fonts/Verdana.ttf");
                break;
        }


        tts_enabled = sharedPrefs.getBoolean("switch_sintetizador",true);


        Log.d("font settings", font_value.toString());
        mensajes_text.setTypeface(font_face);
        juegos_text.setTypeface(font_face);
        ajustes_text.setTypeface(font_face);
        videocnf_text.setTypeface(font_face);
        videos_text.setTypeface(font_face);
        alarma_text.setTypeface(font_face);


        juegos_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    juegos_btn.setBackground(u);
                    juegos_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    juegos_btn.setBackground(d);
                    juegos_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    if (tts_enabled){
                        sintetiza(juegos_text.getText().toString());
                    }
                }

                return false;
            }
        });

        ajustes_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    ajustes_btn.setBackground(u);
                    ajustes_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    ajustes_btn.setBackground(d);
                    ajustes_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    if (tts_enabled){
                        sintetiza(ajustes_text.getText().toString());
                    }
                }

                return false;
            }
        });

        videocnf_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    videocnf_btn.setBackground(u);
                    videocnf_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    videocnf_btn.setBackground(d);
                    videocnf_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    if (tts_enabled){
                        sintetiza(videocnf_text.getText().toString());
                    }
                }

                return false;
            }
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
                    if (tts_enabled){
                        sintetiza(videos_text.getText().toString());
                    }
                }

                return false;
            }
        });

        mensajes_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    mensajes_btn.setBackground(u);
                    mensajes_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    mensajes_btn.setBackground(d);
                    mensajes_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    if (tts_enabled){
                        sintetiza(mensajes_text.getText().toString());
                    }
                }

                return false;
            }
        });

        alarma_btn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    alarma_btn.setBackground(u);
                    alarma_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    alarma_btn.setBackground(d);
                    alarma_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    if (tts_enabled){
                        sintetiza(alarma_text.getText().toString());
                    }
                }

                return false;
            }
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

 /*   protected void onResume(){
        Intent intent= new Intent(this, ReceiverService.class);
        bindService(intent, mConnection,
                Context.BIND_AUTO_CREATE);
        super.onResume();
    }

    protected void onPause() {
        unbindService(mConnection);
        super.onPause();
    }

    public ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,IBinder binder) {
            ReceiverService.MyBinder b = (ReceiverService.MyBinder) binder;
            s = b.getService();
            Log.d("Service", "Conectado");
        }

        public void onServiceDisconnected(ComponentName className) {
            s = null;
        }
   };*/

    public  void onPostResume(){
        if (tts != null){
            tts.stop();
            tts.shutdown();
            tts = new TextToSpeech(this,this);
        }
        tts_enabled = sharedPrefs.getBoolean("switch_sintetizador",true);
        super.onPostResume();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
        }
        return super.onOptionsItemSelected(item);
    }

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

    public void onInit(int status) {

        if ( status == TextToSpeech.SUCCESS ) {

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            Integer language_value = Integer.parseInt(sharedPrefs.getString("select_language", "0"));
            Integer result = 0;
            switch (language_value) {
                case 0:
                    result = tts.setLanguage(SPANISH);
                    break;
                case 1:
                    result = tts.setLanguage(Locale.ENGLISH);
                    break;
                case 2:
                    result = tts.setLanguage(Locale.FRENCH);
                    break;
                case 3:
                    result = tts.setLanguage(Locale.getDefault());
                    break;
            }


            //coloca lenguaje por defecto en el celular, en nuestro caso el lenguaje es aspañol ;)

            Log.d("Idioma", result.toString());
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                Log.d("Main_tts", "This Language is not supported");
            } else {
                Log.d("Main_tts", "This Language is not supported");

            }

        } else {
            Log.d("Main_tts", "Initilization Failed!");
        }
    }

    private void sintetiza( String texto ) {
        tts.speak(texto, TextToSpeech.QUEUE_ADD, null);

    }

    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
