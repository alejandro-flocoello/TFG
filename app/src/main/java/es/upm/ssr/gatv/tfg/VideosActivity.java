package es.upm.ssr.gatv.tfg;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.FileNotFoundException;

public class VideosActivity extends  AppCompatActivity {

    private AdaptadorClass mAdapter;
    private ListView entryList;

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
                playVideo(url);
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    @Override
    protected Void doInBackground(Void... arg0) {
        //Download the file
        try {
            DownloaderUrl.DownloadFromUrl("http://138.4.47.33:2103/afc/home/Mensajes/prueba.xml", openFileOutput("Prueba.xml", Context.MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        //setup our Adapter and set it to the ListView.
        mAdapter = new AdaptadorClass(VideosActivity.this, -1, GatvXmlParser.getStackSitesFromFile(VideosActivity.this));
        entryList.setAdapter(mAdapter);
        Log.i("StackSites", "adapter size = "+ mAdapter.getCount());
    }
}

    public void playVideo(String url) {
        // Do something in response to button Videos

        Intent intent = new Intent(this, VideoPlayerActivity.class);
        startActivity(intent);
    }


}

