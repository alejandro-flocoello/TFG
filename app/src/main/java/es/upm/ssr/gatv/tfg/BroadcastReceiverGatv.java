package es.upm.ssr.gatv.tfg;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class BroadcastReceiverGatv extends BroadcastReceiver {
    private static final long REPEAT_TIME = 1000 * 60;

    public void onReceive(Context context, Intent intent) {
        AlarmManager service = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, ReceiveStartService.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, i, 0);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 30);
        service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(), REPEAT_TIME, pending);
        Log.d("Autostart", "started");
    }
}


