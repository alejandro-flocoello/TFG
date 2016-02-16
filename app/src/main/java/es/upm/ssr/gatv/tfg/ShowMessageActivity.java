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

import java.net.URL;

public class ShowMessageActivity extends AppCompatActivity {

    public static final String DEBUG_MENSAJES = "ShowMessage" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);
        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        //Extract the data…
        String url = bundle.getString("url");
        cargaMensajes(url);
    }

    public void cargaMensajes(String urlString){

        try {
            ImageView messageImg = (ImageView)findViewById(R.id.messageImg);
            TextView messageText = (TextView) findViewById(R.id.messageText);
            URL url = new URL(urlString);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            messageImg.setImageBitmap(bmp);

            Log.d(DEBUG_MENSAJES, "Cargando el mensaje");
        } catch (Exception e){
            // TODO: handle exception
            Log.d(DEBUG_MENSAJES, "Error en la carga del mensaje");
            Toast.makeText(this, "Error al cargar el mensaje", Toast.LENGTH_SHORT).show();
        }
    }

}
