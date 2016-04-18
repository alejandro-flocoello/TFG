package es.upm.ssr.gatv.tfg;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Activity
{
    public static final String DEBUG_SPLASH = "ShowMessage" ;
    private static final long DELAY = 4000;
    private boolean scheduled = false;
    private Timer splashTimer;
    private ReceiverService s;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        cargaInicioPrefs();
        splashTimer = new Timer();
        splashTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                SplashScreen.this.finish();
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
            }
        }, DELAY);
        scheduled = true;
        cargaInicioPrefs();
        comiezaServicio();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scheduled)
            splashTimer.cancel();
        splashTimer.purge();
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


    protected void onResume() {
        super.onResume();
        Intent intent= new Intent(this, ReceiverService.class);
        bindService(intent, mConnection,
                Context.BIND_AUTO_CREATE);
    }
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder binder) {
            ReceiverService.MyBinder b = (ReceiverService.MyBinder) binder;
            s = b.getService();
            Log.d("Splash Service", "Conectado");

        }

        public void onServiceDisconnected(ComponentName className) {
            s = null;
        }
    };


    public void comiezaServicio(){
            if (s != null) {
                s.comienza();
            }
    }





}