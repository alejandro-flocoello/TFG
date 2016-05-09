package es.upm.ssr.gatv.tfg;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ReceiveStartService extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, ReceiverService.class);
        context.startService(service);
    }
}
