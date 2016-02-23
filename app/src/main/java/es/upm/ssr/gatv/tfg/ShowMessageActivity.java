package es.upm.ssr.gatv.tfg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.net.URL;

public class ShowMessageActivity extends AppCompatActivity {

    public static final String DEBUG_MENSAJES = "ShowMessage" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String url = bundle.getString("url");
        String txt = bundle.getString("txt");
        String title = bundle.getString("title");
        cargaMensajes(url,txt);

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

            Log.d(DEBUG_MENSAJES, "Cargando el mensaje");
        } catch (Exception e){
            // TODO: handle exception
            Log.d(DEBUG_MENSAJES, "Error en la carga del mensaje");
            Toast.makeText(this, "Error al cargar el mensaje", Toast.LENGTH_SHORT).show();
        }
    }
}
