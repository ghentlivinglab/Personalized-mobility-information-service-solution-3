package vopro7.vop.mobiliteitgent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;

import vopro7.vop.mobiliteitgent.activities.EventActivity;
import vopro7.vop.mobiliteitgent.activities.LoginActivity;
import vopro7.vop.mobiliteitgent.eventsreceiver.rabbitmq.WebSocketUse;
import vopro7.vop.mobiliteitgent.models.Event;

/**
 * @author App Team
 */
public class PushMessageService extends Service {

    private Thread rabbitMQThread;

    private WebSocketUse receiverSocket;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        String extra = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("user_id", "");
        if (receiverSocket == null && !extra.equals("")) {
            // get user id and create new receiver socket
            final int userId = Integer.valueOf(extra);
            final Context currentContext = this;

            // create new thread and run websocket receiver for rabbitmq messages
            rabbitMQThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // rabbit mq code
                    receiverSocket = new WebSocketUse(userId, currentContext);
                    receiverSocket.startReceiving();
                }
            });
            rabbitMQThread.start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // stop receiving
        receiverSocket.stopReceiving();
        // stop thread
        if (rabbitMQThread.isAlive()) {
            rabbitMQThread.interrupt();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
