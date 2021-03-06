package es.upm.ssr.gatv.tfg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Locale;

public class ShowMessageActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    public static final String DEBUG_MENSAJES = "ShowMessage" ;
    public static final String DEBUG_TTS = "TTS" ;
    private final Locale SPANISH = new Locale("es","ES");
    private TextToSpeech tts;
    private Button btnSpeak;
    private Button btnReproduce;
    private Button btnStop;
    private Button btnPausa;
    private Button btnVideoAdd;
    private String messageText;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        tts = new TextToSpeech(this,this);


        setContentView(R.layout.activity_show_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        //Extract the data…
        String img_msg = bundle.getString("img_msg");
        String txt = bundle.getString("txt");
        String title = bundle.getString("title");
        String clip_audio = bundle.getString("clip_audio");
        String video_add = bundle.getString("video_add");

        btnReproduce = (Button) findViewById(R.id.buttonReproduccion);
        btnVideoAdd = (Button) findViewById(R.id.btnVideoAdd);
        btnStop = (Button) findViewById(R.id.buttonStop);
        btnSpeak = (Button) findViewById(R.id.btnSpeak);
        btnPausa = (Button) findViewById(R.id.buttonPausa);

        cargaMensajes(title, img_msg, txt, clip_audio, video_add);

        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        Boolean tts_enabled = sharedPrefs.getBoolean("switch_sintetizador",true);

        if (tts_enabled){
                btnSpeak.setVisibility(View.VISIBLE);
        } else{
                btnSpeak.setVisibility(View.INVISIBLE);
        }


        messageText = txt.toString();
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sintetiza(messageText);
            }

        });
        btnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Toast.makeText(getApplicationContext(), "Parado. Pulse reproducir para volver a escuchar",Toast.LENGTH_SHORT).show();
                mediaPlayer.stop();
                mediaPlayer = null;
                btnReproduce.setEnabled(true);
                btnPausa.setEnabled(false);
                btnStop.setEnabled(false);
            }

        });


        btnPausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pausa. Pulse reproducir para continuar escuchando",Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                Log.d(DEBUG_MENSAJES, "isPlaying() : " + mediaPlayer.isPlaying());
                btnStop.setEnabled(false);
                btnReproduce.setEnabled(true);
                btnPausa.setEnabled(false);
            }
        });



        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void cargaMensajes(final String title,final String urlString, String txt, final String clipAudio,final String videoAdd){

        try {

            ImageLoader imageLoader = ImageLoader.getInstance();
            ImageView messageImg = (ImageView)findViewById(R.id.messageImg);
            TextView messageText = (TextView) findViewById(R.id.messageText);
            TextView textClipAudio = (TextView) findViewById(R.id.textClipAudio);
            TextView textVideoAdd = (TextView) findViewById(R.id.textVideoAdd);

            //Button btnSpeak = (Button) findViewById(R.id.btnSpeak);

            if(clipAudio != null){
                btnReproduce.setVisibility(View.VISIBLE);
                btnReproduce.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        reproduceAudio(clipAudio);
                        btnReproduce.setEnabled(false);
                        btnStop.setEnabled(true);
                        btnPausa.setEnabled(true);
                    }

                });}
            else{btnReproduce.setVisibility(View.INVISIBLE);
                btnStop.setVisibility(View.INVISIBLE);
                btnPausa.setVisibility(View.INVISIBLE);
                textClipAudio.setVisibility(View.INVISIBLE);
            }

            if(videoAdd != null){
                btnVideoAdd.setVisibility(View.VISIBLE);
                btnVideoAdd.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        playVideo(videoAdd,title);
                    }

                });


            }
            else{btnVideoAdd.setVisibility(View.INVISIBLE);
                textVideoAdd.setVisibility(View.INVISIBLE);
                }


            messageText.setMovementMethod(new ScrollingMovementMethod());
            imageLoader.displayImage(urlString, messageImg);
            messageText.setText(txt);
            SharedPreferences sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this);
            Integer myNum = Integer.parseInt(sharedPrefs.getString("set_text_list", "18"));
            messageText.setTextSize(myNum);
            Log.d(DEBUG_MENSAJES, "Cargando el mensaje");
        } catch (Exception e){
            // TODO: handle exception
            Log.d(DEBUG_MENSAJES, "Error en la carga del mensaje");
            Toast.makeText(this, "Error al cargar el mensaje", Toast.LENGTH_SHORT).show();
        }


    }

    public void playVideo(String url, String title) {
        // Do something in response to button Videos
        Bundle bundle = new Bundle();

        //Add your data to bundle
        bundle.putString("url", url);
        bundle.putString("title", title);

        //Add the bundle to the intent

        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
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
                btnSpeak.setEnabled(false);

                Log.d(DEBUG_TTS, "This Language is not supported");
            } else {
                btnSpeak.setEnabled(true);

            }

        } else {
            Log.d(DEBUG_TTS, "Initilization Failed!");
        }
    }


    private void sintetiza( String texto ) {
        tts.speak( texto, TextToSpeech.QUEUE_ADD, null );
        Log.d(DEBUG_TTS, "Dime algo");
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

    public void reproduceAudio(String urlString){

        try {

            Log.d(DEBUG_MENSAJES, "mediaPlayer : " + mediaPlayer);

            if(mediaPlayer == null ) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(urlString);
                mediaPlayer.prepare();
                mediaPlayer.start();
                Toast.makeText(getApplicationContext(), "Reproduciendo",Toast.LENGTH_SHORT).show();
            }else{
                mediaPlayer.start();
                Toast.makeText(getApplicationContext(), "Reproduciendo",Toast.LENGTH_SHORT).show();
                Log.d(DEBUG_MENSAJES, "isPlaying() : " + mediaPlayer.isPlaying());
            }

            Log.d(DEBUG_MENSAJES, "Reproduccion en marcha");
        } catch (Exception e){
            // TODO: handle exception
            Log.d(DEBUG_MENSAJES, "Error en la reproduccion");
            Toast.makeText(this, "Error al reproducir el clip de audio", Toast.LENGTH_SHORT).show();
        }
    }

}