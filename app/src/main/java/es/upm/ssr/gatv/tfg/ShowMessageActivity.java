package es.upm.ssr.gatv.tfg;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.net.URL;
import java.util.Locale;

public class ShowMessageActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    public static final String DEBUG_MENSAJES = "ShowMessage" ;
    private TextToSpeech tts;
    private Button btnSpeak;
    private String messageText;


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
        cargaMensajes(img_msg, txt);

        messageText = txt.toString();
        btnSpeak = (Button) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sintetiza(messageText);
            }

        });

        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void cargaMensajes(String urlString, String txt){

        try {

            ImageLoader imageLoader = ImageLoader.getInstance();
            ImageView messageImg = (ImageView)findViewById(R.id.messageImg);
            TextView messageText = (TextView) findViewById(R.id.messageText);
            imageLoader.displayImage(urlString, messageImg);
            messageText.setText(txt);
            SharedPreferences sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this);
            Integer myNum = Integer.parseInt(sharedPrefs.getString("set_text_list", "NULL"));
            messageText.setTextSize(myNum);
            Log.d(DEBUG_MENSAJES, "Cargando el mensaje");
        } catch (Exception e){
            // TODO: handle exception
            Log.d(DEBUG_MENSAJES, "Error en la carga del mensaje");
            Toast.makeText(this, "Error al cargar el mensaje", Toast.LENGTH_SHORT).show();
        }


    }


    //Inicia TTS
    @Override
    public void onInit(int status) {

        if ( status == TextToSpeech.SUCCESS ) {

            //coloca lenguaje por defecto en el celular, en nuestro caso el lenguaje es aspañol ;)
            int result = tts.setLanguage( Locale.getDefault() );

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                btnSpeak.setEnabled(false);

                Log.e("TTS", "This Language is not supported");
            } else {
                btnSpeak.setEnabled(true);

            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }


    private void sintetiza( String texto ) {
        tts.speak( texto, TextToSpeech.QUEUE_FLUSH, null );
        Log.e("TTS", "Dime algo");
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