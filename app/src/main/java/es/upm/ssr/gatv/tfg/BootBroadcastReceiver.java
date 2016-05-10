package es.upm.ssr.gatv.tfg;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class BootBroadcastReceiver extends WakefulBroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "es.upm.ssr.gatv.tfg.MyTestService";
    public BootBroadcastReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        // Launch the specified service when this message is received
        Intent startServiceIntent = new Intent(context, MyTestService.class);
        startWakefulService(context, startServiceIntent);
    }
}
