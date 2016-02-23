package es.upm.ssr.gatv.tfg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Activity
{
    private static final long DELAY = 4000;
    private boolean scheduled = false;
    private Timer splashTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scheduled)
            splashTimer.cancel();
        splashTimer.purge();
    }
}