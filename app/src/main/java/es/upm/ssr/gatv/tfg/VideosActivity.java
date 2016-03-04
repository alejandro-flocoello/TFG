package es.upm.ssr.gatv.tfg;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;

import es.upm.ssr.gatv.tfg.Entry;

public class VideosActivity extends  AppCompatActivity {

    private AdaptadorClass mAdapter;
    private ListView entryList;
    public boolean server;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("EntryList", "OnCreate()");
        setContentView(R.layout.activity_videos);

        //Get reference to our ListView
        entryList = (ListView)findViewById(R.id.entryList);

        //Set the click listener to launch the browser when a row is clicked.
        entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                String url = mAdapter.getItem(pos).getLink();
                String title = mAdapter.getItem(pos).getTitle();

                playVideo(url, title);
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);

            }

        });

        if(isNetworkAvailable()){
            Log.i("EntryList", "comienza download Task");
            EntryDownloadTask download = new EntryDownloadTask();
            download.execute();
        }else{
            mAdapter = new AdaptadorClass(getApplicationContext(), -1, GatvXmlParser.getStackSitesFromFile(VideosActivity.this));
            entryList.setAdapter(mAdapter);
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Actualizando contenido ...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                refreshButton();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class EntryDownloadTask extends AsyncTask<Void, Void, Void> {

        public boolean server;

    @Override
    protected Void doInBackground(Void... arg0) {
        //Download the file
        try {
            DownloaderUrl.DownloadFromUrl("http://138.4.47.33:2103/afc/home/Mensajes/Contenido/mensajes.xml", openFileOutput("Mensajes.xml", Context.MODE_PRIVATE));
            server = DownloaderUrl.setServer();
            Log.i("StackSites", "Server value background= " + server);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("StackSites", "Server value onPostExecute = " + server);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        //setup our Adapter and set it to the ListView.
        mAdapter = new AdaptadorClass(VideosActivity.this, -1, GatvXmlParser.getStackSitesFromFile(VideosActivity.this));
        entryList.setAdapter(mAdapter);
        Log.i("StackSites", "adapter size = " + mAdapter.getCount());
        Log.i("StackSites", "Server value = " + server);
        if (!server){
            displayAlert();
        }

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

    public  void refreshButton(){
        Intent intent = getIntent();
        overridePendingTransition(android.R.anim.cycle_interpolator, android.R.anim.cycle_interpolator);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(intent);
    }

    private void displayAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("No ha sido posible cargar la lista de videos. Posible servidor caido");
        alertDialogBuilder.setTitle("ERROR");
        alertDialogBuilder.setIcon(R.drawable.ic_action_warning);
        alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                overridePendingTransition(android.R.anim.cycle_interpolator, android.R.anim.cycle_interpolator);
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


}

