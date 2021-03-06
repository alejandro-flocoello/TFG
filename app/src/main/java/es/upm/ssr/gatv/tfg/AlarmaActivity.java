package es.upm.ssr.gatv.tfg;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


public class AlarmaActivity  extends AppCompatActivity{
    private static final String DEBUG_TAG = "NetworkStatus";
    private static final String TAG = "Alarma";
    String http = "http://138.4.47.33:2103/afc/home/alarma.php";
    Calendar cal ;
    String msgInitial;
    public boolean flag = true;
    public TextView textMsgAlarma;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textMsgAlarma =  (TextView) findViewById(R.id.textMsgAlarma);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cal = Calendar.getInstance();
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        msgInitial = sharedPrefs.getString("example_text","USUARIO");

        if(isNetworkAvailable()){
            Log.i("EntryList", "comienza download AlarmaTask");
            AlarmaTask alarma = new AlarmaTask();
            alarma.execute();
        }else{
            Toast.makeText(getApplicationContext(),"No ha sido posible contactar con el Servidor. Compruebe su conexion", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }





    private class AlarmaTask extends AsyncTask<Void, Void, Void> {
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection conn = null;


        protected Void doInBackground(Void... arg0) {

            try {

                URL url = new URL(http);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user", msgInitial);
                Log.d("Alarma_user",msgInitial);
                jsonObject.put("fecha",String.valueOf( cal.getTime()));
                Log.d("Alarma_Date", String.valueOf(cal.getTime()));
                String message = jsonObject.toString();
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(message.getBytes().length);

                //make some HTTP header nicety
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                //open
                conn.connect();
                //setup send
                os = new BufferedOutputStream(conn.getOutputStream());
                os.write(message.getBytes());
                //clean up
                os.flush();
                //do somehting with response
                is = conn.getInputStream();
                //String contentAsString = readIt(is,len);
                flag = true;
            } catch (IOException e) {
                e.printStackTrace();
                flag = false;
            } catch (JSONException e) {
                e.printStackTrace();
                flag = false;
            } finally {
                //clean up
                try {
                    os.close();
                    is.close();
                    flag = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    flag = false;
                }

                conn.disconnect();
            } return null;

        }

        @Override
        protected void onPostExecute(Void result){
            Toast.makeText(getApplicationContext(),"Contactando con el SERVIDOR", Toast.LENGTH_SHORT).show();
            if (flag){
                textMsgAlarma.setText(R.string.alarm_msg);
            } else{
                textMsgAlarma.setText(R.string.alarm_msg_fail);
            }
        }
    }




}