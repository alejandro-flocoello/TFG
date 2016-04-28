package es.upm.ssr.gatv.tfg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class TresEnRayaMenu extends AppCompatActivity  implements TextToSpeech.OnInitListener {

    private Typeface font_face;
    private TextToSpeech tts;
    private final Locale SPANISH = new Locale("es","ES");
    private Boolean tts_enabled = false;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tres_en_raya_menu);
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
        TextView texto = (TextView) findViewById(R.id.titulo);
        Animation animacion = AnimationUtils.loadAnimation(this, R.anim.girozoom);
        texto.startAnimation(animacion);

        final Button jugar = (Button) findViewById(R.id.jugar);
        Animation animacion2 = AnimationUtils.loadAnimation(this,R.anim.aparecer);
        jugar.startAnimation(animacion2);

        final Button config = (Button) findViewById(R.id.acercaDe);
        Animation animacion3 = AnimationUtils.loadAnimation(this, R.anim.desplazarderecha);
        config.startAnimation(animacion3);

        final Button acercade = (Button) findViewById(R.id.salir);
        Animation animacion4 = AnimationUtils.loadAnimation(this,R.anim.aparecer);
        acercade.startAnimation(animacion4);
        final Button jugarCpu = (Button) findViewById(R.id.jugarCpu);

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
        texto.setTypeface(font_face);
        jugar.setTypeface(font_face);
        config.setTypeface(font_face);
        acercade.setTypeface(font_face);

        jugar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (tts_enabled) {
                        sintetiza(jugar.getText().toString());
                    }
                }

                return false;
            }
        });

        jugarCpu.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (tts_enabled) {
                        sintetiza(jugarCpu.getText().toString());
                    }
                }

                return false;
            }
        });
        acercade.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (tts_enabled) {
                        sintetiza(acercade.getText().toString());
                    }
                }

                return false;
            }
        });

        config.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (tts_enabled) {
                        sintetiza(config.getText().toString());
                    }
                }

                return false;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void pulsaSalir(View view){
        finish();
    }

    public void pulsaJugar(View view){
        Intent i = new Intent(this, TresEnRayaActivity.class);
        startActivity(i);
    }

    public void pulsaJugarCpu(View view){

        Intent i = new Intent(this, JuegoCpu.class);
        startActivity(i);
    }

    public void pulsaAcercaDe(View view){

        Intent i = new Intent(this, AcercaDe.class);
        startActivity(i);
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
