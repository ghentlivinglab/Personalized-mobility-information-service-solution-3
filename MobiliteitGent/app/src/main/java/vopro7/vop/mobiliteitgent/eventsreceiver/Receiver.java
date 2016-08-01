/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vopro7.vop.mobiliteitgent.eventsreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import vopro7.vop.mobiliteitgent.R;
import vopro7.vop.mobiliteitgent.activities.EventActivity;
import vopro7.vop.mobiliteitgent.models.Event;


/**
 * @author App Team
 */
public abstract class Receiver {

    private final int userId;

    public Receiver(int userid) {
        this.userId = userid;
    }

    /**
     * Method to make the receiver start receiving.
     */
    public abstract void startReceiving();

    /**
     * Method called when the receiver must stop receiving events and others.
     */
    public abstract void stopReceiving();

    /**
     * Method used to wait untill the receiver has successfully closed all his resources.
     */
    public abstract void waitTillClose();

    /**
     * Method used to parse a received JSON-String representing an event.
     *
     * @param c The context of the receiver
     * @param event An event to precess
     * of the event
     */
    public void processEvent(Context c, String event) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(event);

            Event e = new Event();
            e.loadFromJson(jsonObject);
            createNotification(c, e);
        } catch (JSONException e) {
            // error
        }

    }

    /**
     * Method to get the user-id of the user who demanded this Receiver.
     *
     * @return user-id of the user
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Create the android notification of the received event
     * @param c The current context
     * @param event The event we want a notification for
     */
    public void createNotification(Context c, Event event) {
        // create intent for event when notification is selected
        Intent intent = new Intent(c, EventActivity.class);
        intent.putExtra(EventActivity.EXTRA_EVENT, event);
        PendingIntent pIntent = PendingIntent.getActivity(c, (int) System.currentTimeMillis(), intent, 0);

        Notification not;
        long[] pattern = {500,200,500,200,500};
        if(event.getJams() != null) {
            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            not = new Notification.Builder(c)
                    .setContentTitle(c.getString(R.string.notification))
                    .setContentText(event.toString())
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .setSound(uri)
                    .setVibrate(pattern)
                    .setLights(Color.RED, 500, 500)
                    .build();
        } else {
            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            not = new Notification.Builder(c)
                    .setContentTitle(c.getString(R.string.notification))
                    .setContentText(event.toString())
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .setSound(uri)
                    .setVibrate(pattern)
                    .setLights(Color.BLUE, 500, 500)
                    .build();
        }
        NotificationManager notificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), not);

    }
}
