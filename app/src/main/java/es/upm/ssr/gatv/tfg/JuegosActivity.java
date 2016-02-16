package es.upm.ssr.gatv.tfg;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class JuegosActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "NetworkStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juegos);
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

        Intent intent = getIntent();
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
}
