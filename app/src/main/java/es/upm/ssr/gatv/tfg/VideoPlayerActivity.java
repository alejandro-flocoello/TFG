package es.upm.ssr.gatv.tfg;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class VideoPlayerActivity extends AppCompatActivity {

    //public static final String url = "http://138.4.47.33:2103/afc/home/Mensajes/Videos/homer.mp4";
    public static final String url ="http://138.4.47.33:2103/afc/home/Mensajes/Videos/ding_dong_muri%c3%b3_la_bruja.mp4";
    public static final String DEBUG_REPRODUCE = "VideoPlayer" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)

        .build();
        ImageLoader.getInstance().init(config);
        setContentView(R.layout.activity_video_player);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reproduceVideo(url);
    }

    public void reproduceVideo(String urlString){

        try {
            VideoView videoView = (VideoView) findViewById(R.id.videoView);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            Uri video = Uri.parse(urlString);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.start();
            Log.d(DEBUG_REPRODUCE, "Reproduccion en marcha");
        } catch (Exception e){
            // TODO: handle exception
            Log.d(DEBUG_REPRODUCE, "Error en la reproduccion");
            Toast.makeText(this, "Error al reproducir", Toast.LENGTH_SHORT).show();
        }
    }


}
