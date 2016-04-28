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
    private int posant_vid;

    public ReceiverService() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "RECEIVE SERVICE activado");
        Log.i("EntryList", "comienza download Task");
        comienza();
        sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        editor = sharedPrefs.edit();
        //Get reference to our ListView
        posant_msg = sharedPrefs.getInt("posact_msg", 0);
        posant_vid = sharedPrefs.getInt("posact_video", 0);
        return Service.START_NOT_STICKY;
    }

    public void onDestroy() {

        if (mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
        };Log.d(TAG, "onDestroy");
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

        private int posactual = 0;
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
            posactual= msg_entry.size();
            editor.putInt("posact_msg",posactual);
            editor.commit();
            Log.d("Pos.actual msg", Integer.toString(posactual));
            switch_notification = sharedPrefs.getBoolean("notifications_new_message",false);

            if ((posactual - posant_msg) > 0) {
                if(switch_notification){
                    alarm();}
                notify_mensajes("Nuevo Mensaje", "Hay nuevo(s) mensaje(s) disponible en Mensajes");
            }

        }

    }

    public class EntryDownloadTaskVideo extends AsyncTask<Void, Void, Void> {

        private int posactual = 0;
        private boolean switch_notification=false;
        private List<Entry> msg_videos;

        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            try {
                DownloaderUrl.DownloadFromUrl("http://138.4.47.33:2103/afc/home/Mensajes/Contenido/videos.xml", openFileOutput("Videos.xml", Context.MODE_PRIVATE));
                Log.i(TAG,"Descargando Videos");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            Log.i(TAG,"Descargado Videos");
            msg_videos = GatvXmlParser.getStackSitesFromFile(ReceiverService.this);
            posactual= msg_videos.size();
            editor.putInt("posact_msg",posactual);
            editor.commit();
            Log.d("Pos.actual video", Integer.toString(posactual));
            switch_notification = sharedPrefs.getBoolean("notifications_new_message",false);

            if ((posactual - posant_vid) > 0) {
                if(switch_notification){
                    alarm();}
                notify_video("Nuevo Video", "Hay nuevo contenido disponible en Videos");
            }

        }

    }

    public void comienza(){
        EntryDownloadTask download = new EntryDownloadTask();
        EntryDownloadTaskVideo downloadVideo = new EntryDownloadTaskVideo();
        download.execute();downloadVideo.execute();
    }

    private void alarm(){
        SharedPreferences getAlarms = PreferenceManager.getDefaultSharedPreferences(this);
        String alarms = getAlarms.getString("notifications_new_message_ringtone", "default ringtone");
        Uri uri = Uri.parse(alarms);
        playSound(this, uri);

        //call mMediaPlayer.stop(); when you want the sound to stop
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
    private void notify_video(String notificationTitle, String notificationMessage){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        Intent notificationIntent = new Intent(this,VideosActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this);
        Notification notification = builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_videos).setTicker(notificationTitle).setWhen(System.currentTimeMillis())
                .setAutoCancel(true).setContentTitle(notificationTitle)
                .setContentText(notificationMessage).build();
        notificationManager.notify(9999, notification);


    }
}
