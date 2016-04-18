package es.upm.ssr.gatv.tfg;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.FileNotFoundException;

public class ReceiverService extends Service {

    private static final String TAG = "ReceiverService";
    private final IBinder mBinder = new MyBinder();

    public ReceiverService() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "RECEIVE SERVICE activado");
        Log.i("EntryList", "comienza download Task");

        return Service.START_NOT_STICKY;
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        ReceiverService getService() {
            return ReceiverService.this;
        }
    }


    public class EntryDownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            try {
                DownloaderUrl.DownloadFromUrl("http://138.4.47.33:2103/afc/home/Mensajes/Contenido/mensajes.xml", openFileOutput("Mensajes.xml", Context.MODE_PRIVATE));
                Log.i(TAG,"Descargando");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            Log.i(TAG,"Descargado");


        }

    }

    public void comienza(){
        EntryDownloadTask download = new EntryDownloadTask();
        download.execute();
    }

}
