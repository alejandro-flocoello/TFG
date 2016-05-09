package es.upm.ssr.gatv.tfg;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ReceiverService extends Service {

    private static final String TAG = "ReceiverService";
    private final IBinder mBinder = new MyBinder();
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    private MediaPlayer mMediaPlayer;
    private int posant_msg;
    private int  posactual_msg;
    private EntryDownloadTask download = new EntryDownloadTask();
    public ReceiverService() {
    }

    public void onCreate() {
        Log.d(TAG, "RECEIVE Service activado");
        Log.i("EntryList", "comienza download Task");
        download.execute();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPrefs.edit();
        posant_msg = sharedPrefs.getInt("posact_msg", 0);
        Log.d("Pos.ant msg", Integer.toString(posant_msg));
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "StarCommand activado");
        Log.i("EntryList", "comienza download Task");
        download.execute();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPrefs.edit();
        posant_msg = sharedPrefs.getInt("posact_msg", 0);
        return Service.START_STICKY;
    }

    public void onDestroy() {

        if (mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
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


        private boolean switch_notification=false;
        private List<Entry> msg_entry;

        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            try {
                DownloaderUrl.DownloadFromUrl("http://138.4.47.33:2103/afc/home/Mensajes/Contenido/mensajes.xml", openFileOutput("Mensajes.xml", Context.MODE_PRIVATE));
                Log.i(TAG,"Descargando Mensajes");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            Log.i(TAG,"Descargado Msg");
            msg_entry = GatvXmlParser.getStackSitesFromFile(ReceiverService.this);
            posactual_msg= msg_entry.size();
            editor.putInt("posact_msg",posactual_msg);
            editor.commit();
            Log.d("Pos.actual msg", Integer.toString(posactual_msg));
            switch_notification = sharedPrefs.getBoolean("notifications_new_message",false);

            if ((posactual_msg - posant_msg) > 0) {
                if(switch_notification){
                    alarm();}
                notify_mensajes("Nuevo Mensaje", "Hay nuevo(s) mensaje(s) disponible en Mensajes");
            }

        }

    }

    private void alarm(){
        SharedPreferences getAlarms = PreferenceManager.getDefaultSharedPreferences(this);
        String alarms = getAlarms.getString("notifications_new_message_ringtone", "default ringtone");
        Uri uri = Uri.parse(alarms);
        playSound(this, uri);
    }

    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }

    private void notify_mensajes(String notificationTitle, String notificationMessage){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this,MensajesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this);
        Notification notification = builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_action_gmail).setTicker(notificationTitle).setWhen(System.currentTimeMillis())
                .setAutoCancel(true).setContentTitle(notificationTitle)
                .setContentText(notificationMessage).build();
        notificationManager.notify(8888, notification);


    }
  }
