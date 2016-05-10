package es.upm.ssr.gatv.tfg;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Activity
{
    public static final String DEBUG_SPLASH = "ShowMessage" ;
    private static final long REPEAT_TIME = 1000 * 60;
    private static final long DELAY = 4000;
    private boolean scheduled = false;
    private Timer splashTimer;
//    public ReceiverService s;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        cargaInicioPrefs();
        splashTimer = new Timer();
        splashTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SplashScreen.this.finish();
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
            }
        }, DELAY);
        scheduled = true;
        scheduleAlarm();
    }

    @Override
    protected void onDestroy() {
        if (scheduled)
            splashTimer.cancel();
        splashTimer.purge();
        super.onDestroy();
    }

    public void cargaInicioPrefs(){
        try{
            TextView splashText = (TextView) findViewById(R.id.textSplashView);
            SharedPreferences sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this);
            String msgInitial = sharedPrefs.getString("example_text","USUARIO");
            Log.d(DEBUG_SPLASH, "Usuario:" + msgInitial);
            String bienvenido = getString(R.string.splashText);
            splashText.setText(bienvenido +"\n"+ msgInitial);
        } catch (Exception e){

        }
    }

//    protected void onResume(){
//        Intent intent= new Intent(this, ReceiverService.class);
//        bindService(intent, mConnection,
//                Context.BIND_AUTO_CREATE);
//        super.onResume();
//    }
//
//    protected void onPause() {
//        unbindService(mConnection);
//        super.onPause();
//    }
//
//    public ServiceConnection mConnection = new ServiceConnection() {
//
//        public void onServiceConnected(ComponentName className, IBinder binder) {
//            ReceiverService.MyBinder b = (ReceiverService.MyBinder) binder;
//            s = b.getService();
//            Log.d("Service", "Conectado");
//        }
//
//        public void onServiceDisconnected(ComponentName className) {
//            s = null;
//        }
//    };

    // Launching the service
    public void onStartService(View v) {
        Intent i = new Intent(this, MyTestService.class);
        i.putExtra("foo", "bar");
        startService(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter(MyTestService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(testReceiver, filter);
        // or `registerReceiver(testReceiver, filter)` for a normal broadcast
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(this).unregisterReceiver(testReceiver);
        // or `unregisterReceiver(testReceiver)` for a normal broadcast
    }

    // Define the callback for what to do when data is received
    private BroadcastReceiver testReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if (resultCode == RESULT_OK) {
                String resultValue = intent.getStringExtra("resultValue");
            }
        }
    };

    // Setup a recurring alarm every half hour
    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), BootBroadcastReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, BootBroadcastReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                REPEAT_TIME, pIntent);
    }
}