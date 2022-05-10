package com.apps.freeroadingdriver.push;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import com.apps.freeroadingdriver.R;
import com.apps.freeroadingdriver.activities.DashboardActivity;
import com.apps.freeroadingdriver.constants.AppConstant;
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONException;
import org.json.JSONObject;
public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = MessagingService.class.getSimpleName();
    String type;
    public static void cancelNotification(Context ctx) {
        try {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
            nMgr.cancelAll();
        } catch (Exception e) {

        }

    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData());
        //{content-available=1, action=6, status=6, app_appointment_id=11, alert=You have got appointment from OGGO Taxi, sound=default, message=You have got appointment from OGGO Taxi, base_fare_type=1, appointment_type=1}
        //Calling method to generate notification
        JSONObject mObject = new JSONObject(remoteMessage.getData());
        try {
            final String message = mObject.optString("message");
            final String action = mObject.optString("action");
            String app_appointment_id = mObject.optString("app_appointment_id");
            if (mObject.has("type"))
                type = mObject.getString("type");

            if (action.equals("6") || action.equals("9")) {
                Intent intent = new Intent(AppConstant.RIDE_REQUEST_COMES);
                intent.putExtra("response", action);
                intent.putExtra("appointment_id", app_appointment_id);
                intent.putExtra("message", message);
                intent.putExtra("type", type);
                sendBroadcast(intent);
            }
            else if(action.equals("1")){
                Intent intent = new Intent("new_booking");
                intent.putExtra("response", action);
                intent.putExtra("message", message);
                intent.putExtra("type", type);
                sendBroadcast(intent);
            }
            else if (action.equals("15")) {


                Intent intent = new Intent("invoice");
                intent.putExtra("action", action);
                intent.putExtra("message", message);
                sendBroadcast(intent);


            } else if (action.equals("14")) {

                Intent intent = new Intent("invoice");
                intent.putExtra("action", action);
                intent.putExtra("message", message);
                sendBroadcast(intent);

            }
            sendNotification(message, action, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String messageBody, String action, String type) {
        Intent intent;
        if (type != null && type.equals("road-trip")) {
            FreeRoadingPreferenceManager.getInstance().setRideRoadType(true);
            intent = new Intent(this, DashboardActivity.class);
            intent.putExtra(AppConstant.IS_ROAD_TRIP, true);

        } else { // local-ride
            intent = new Intent(this, DashboardActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(action), intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_appicon);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(largeIcon)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_notiicon : R.mipmap.ic_appicon;
    }
}
