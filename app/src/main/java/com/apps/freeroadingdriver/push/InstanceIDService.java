package com.apps.freeroadingdriver.push;

import android.util.Log;

import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager;


/**
 * Created by Atiqul Alam on 09/10/16.
 */
/*
public class InstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = InstanceIDService.class.getSimpleName();

    */
/**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     *//*

    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        updatePushTokenOnServer(refreshedToken);
        // Notify UI that registration has completed, so the progress indicator can be hidden.
      */
/*  Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);*//*


    }
    // [END refresh_token]

    */
/**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     *//*

    private void updatePushTokenOnServer(String token) {
        FreeRoadingPreferenceManager.getInstance().setDeviceToken(token);
        if (FreeRoadingPreferenceManager.getInstance().isLogin()) {
//            BackgroundExecutor.getInstance().execute(new UpdatePushTokenRequester(new BaseRequest(true, true, false, false)));
        }
    }
}
*/
