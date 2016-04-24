package es.upm.ssr.gatv.tfg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Locale;

public class JuegosActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final String DEBUG_TAG = "NetworkStatus";
    private static final int RESULT_SETTINGS = 1;
    private Typeface font_face;
    private TextToSpeech tts;
    private final Locale SPANISH = new Locale("es","ES");
    private Boolean tts_enabled = false;
    private SharedPreferences sharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juegos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tts = new TextToSpeech(this,this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable u = getResources().getDrawable(R.drawable.border_icon_4);
        final Drawable d = getResources().getDrawable(R.drawable.border_tictactoe);

        final ImageButton buttonTresRaya = (ImageButton) findViewById(R.id.buttonTresEnRaya);
        final TextView textTresRaya = (TextView) findViewById(R.id.textMenuTresEnRaya);

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
        textTresRaya.setTypeface(font_face);

        buttonTresRaya.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    buttonTresRaya.setBackground(u);
                    textTresRaya.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    buttonTresRaya.setBackground(d);
                    textTresRaya.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    if (tts_enabled) {
                        sintetiza(textTresRaya.getText().toString());
                    }
                }

                return false;
            }
        });



        isOnline();
        checkConnection();
    }

    public void isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean conectado = (networkInfo != null && networkInfo.isConnected());
        Log.d(DEBUG_TAG, "Conectado: " + conectado);

    }


    private void checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null) { // connected to the internet
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                boolean isWifiConn = networkInfo.isConnected();
                Log.d(DEBUG_TAG, "Wifi conectado: " + isWifiConn);
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                // connected to the mobile provider's data plan
                boolean isEthernetConn = networkInfo.isConnected();
                Log.d(DEBUG_TAG, "Ethernet conectado: " + isEthernetConn);
            }
        } else {
            // not connected to the internet
            Log.d(DEBUG_TAG, "App is not connected to the Internet ");
        }
    }

    public void muestraTresEnRaya(View view) {
        // Do something in response to button VideoConferencia
        Intent intent = new Intent(this, TresEnRayaMenu.class);
        startActivity(intent);
    }

    public  void onPostResume(){
        if (tts != null){
            tts.stop();
            tts.shutdown();
            tts = new TextToSpeech(this,this);
        }
        tts_enabled = sharedPrefs.getBoolean("switch_sintetizador",true);
        super.onPostResume();
    }
    //Inicia TTS
    @Override
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

    //Cuando se cierra la aplicacion se destruye el TTS
    @Override


    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


}


