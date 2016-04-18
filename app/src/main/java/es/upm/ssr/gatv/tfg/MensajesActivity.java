package es.upm.ssr.gatv.tfg;

        import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;


public class MensajesActivity extends  AppCompatActivity{

    private AdaptadorClass mAdapter;
    private ListView entryListMensajes;
    private MediaPlayer mMediaPlayer;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private int posant;
    private ReceiverService s;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("EntryList", "OnCreate()");
        setContentView(R.layout.activity_mensajes);

        sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        editor = sharedPrefs.edit();

        //Get reference to our ListView
        entryListMensajes = (ListView)findViewById(R.id.entryListMensajes);
        posant = sharedPrefs.getInt("posact_msg", 0);
        Log.d("Pos.ant", Integer.toString(posant));
        //Set the click listener to launch the browser when a row is clicked.
        entryListMensajes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                String img_msg = mAdapter.getItem(pos).getImgMsgUrl();
                String txt = mAdapter.getItem(pos).getSummary();
                String title = mAdapter.getItem(pos).getTitle();
                String audio = mAdapter.getItem(pos).getAudioUrl();
                String videoAdd = mAdapter.getItem(pos).getLink();
                showMensaje(img_msg, txt, title, videoAdd, audio);

            }

        });

        if(isNetworkAvailable()){
            Log.i("EntryList", "comienza download Task");
            EntryDownloadTask download = new EntryDownloadTask();
            download.execute();
        }else{
            mAdapter = new AdaptadorClass(getApplicationContext(), -1, GatvXmlParser.getStackSitesFromFile(MensajesActivity.this));
            entryListMensajes.setAdapter(mAdapter);
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
        //mensajesAlarma();
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onPostResume(){
        if (mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        super.onPostResume();
    }

    private class EntryDownloadTask extends AsyncTask<Void, Void, Void> {

        public boolean server;
        private int posactual = 0;
        private boolean switch_notification=false;
        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            try {
                DownloaderUrl.DownloadFromUrl("http://138.4.47.33:2103/afc/home/Mensajes/Contenido/mensajes.xml", openFileOutput("Mensajes.xml", Context.MODE_PRIVATE));
                server = DownloaderUrl.setServer();
                Log.i("StackSites", "Server value background= " + server);
                } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            //setup our Adapter and set it to the ListView.
            mAdapter = new AdaptadorClass(MensajesActivity.this, -1, GatvXmlParser.getStackSitesFromFile(MensajesActivity.this));
            entryListMensajes.setAdapter(mAdapter);
            Log.i("StackSites", "Server value onPostExecute = " + server);
            posactual = mAdapter.getCount();
            editor.putInt("posact_msg",posactual);
            editor.commit();
            if (!server){
                displayAlert();}
            Log.d("Pos.actual", Integer.toString(posactual));
            switch_notification = sharedPrefs.getBoolean("notifications_new_message",false);

            if ((posactual - posant) > 0) {
                if(switch_notification){
                alarm();}
                notify_mensajes("Nuevo Mensaje", "Hay nuevo(s) mensaje(s) disponible en Mensajes");
            }
        }
    }

    public void showMensaje(String img_msg , String txt , String title, String videoAdd , String audio) {
        // Do something in response to button Mensajes
        Bundle bundle = new Bundle();

        //Add your data to bundle
        bundle.putString("img_msg", img_msg);
        bundle.putString("txt", txt);
        bundle.putString("title", title);
        bundle.putString("clip_audio",audio);
        bundle.putString("video_add", videoAdd);

        //Add the bundle to the intent

        Intent intent = new Intent(this, ShowMessageActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public  void refreshButton(){
        Intent intent = getIntent();
        //overridePendingTransition(android.R.anim.cycle_interpolator, android.R.anim.cycle_interpolator);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(intent);
    }

    private void displayAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("No ha sido posible cargar la lista de mensajes. Posible servidor caido");
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

    public void mensajesAlarma(){
        Intent myIntent = new Intent(this , ReceiveStartService.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 30);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 2 * 60 * 1000, pendingIntent);  //set repeating every  2 minutes
    }




}

