package com.apps.freeroadingdriver.pubnub;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNHeartbeatNotificationOptions;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.enums.PNReconnectionPolicy;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Admin on 10/17/2017.
 */

public class PubNubManager {
    private static final String TAG = PubNubManager.class.getSimpleName();

    private static PubNubManager pubnubManager;
    private static PNConfiguration pnConfiguration;
    private static String SUBSCRIBE_KEY;
    private static String PUBLISH_KEY;
    private PubNub pubNub;

    public static void init(String subscribeKey, String publishKey) throws PubnubConfigurationException {
        if (TextUtils.isEmpty(subscribeKey)
                || TextUtils.isEmpty(publishKey)) {
            Log.d(TAG, "Pubnub Configuration failed");
            pnConfiguration = null;
            throw new PubnubConfigurationException();
        }

        if (pnConfiguration != null) {
            Log.d(TAG, "Pubnub Configuration failed");
            return;
        }


        PubNubManager.SUBSCRIBE_KEY = subscribeKey.trim();
        PubNubManager.PUBLISH_KEY = publishKey.trim();

        pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(SUBSCRIBE_KEY);
        pnConfiguration.setPublishKey(PUBLISH_KEY);
        //pnConfiguration.setUuid(GoGoBikeApp.getInstance().getDeviceID());

        pnConfiguration.setReconnectionPolicy(PNReconnectionPolicy.LINEAR);
        pnConfiguration.setLogVerbosity(PNLogVerbosity.BODY); // REMOVE WHEN CREATE RELEASE BUILD
        pnConfiguration.setHeartbeatNotificationOptions(PNHeartbeatNotificationOptions.ALL);

        Log.d(TAG, "Pubnub Configuration done");
    }

    public void clearPubnub() {
        if (pubNub != null) {
            pubNub.stop();
        }
        PubNubManager.SUBSCRIBE_KEY = null;
        PubNubManager.PUBLISH_KEY = null;
        pnConfiguration = null;
        pubNub = null;
    }


    public static PubNubManager getPubnubManager() {
        if (pubnubManager == null) {
            pubnubManager = new PubNubManager();
        }
        return pubnubManager;
    }

    private PubNub initPubNub() throws PubnubConfigurationException {
        if (pnConfiguration == null) {
            throw new PubnubConfigurationException();
        }
        if (pubNub == null) {
            pubNub = new PubNub(PubNubManager.pnConfiguration);
        }
        return pubNub;
    }

    public PubNub getPubNub() throws PubnubConfigurationException, PubnubNullException {
        pubNub = initPubNub();
        if (pubNub == null) {
            throw new PubnubNullException();
        }
        return pubNub;
    }

    public void subscribe(List<String> channelLIst, SubscribeCallback subscribeCallback) {
        PubNub pubNub = null;
        try {
            pubNub = getPubNub();
            pubNub.addListener(subscribeCallback);
            pubNub.subscribe().channels(channelLIst).execute();
        } catch (PubnubConfigurationException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        } catch (PubnubNullException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
    }

    public void publish(JsonObject message, String channalName) throws JSONException {
        try {
            PubNub pubNub = null;
            pubNub = getPubNub();
            pubNub.publish()
                    .message(message)
                    .channel(channalName)
                    .async(new PNCallback<PNPublishResult>() {
                        @Override
                        public void onResponse(PNPublishResult result, PNStatus status) {
                            if(!status.isError()) {
                                System.out.println("pub timetoken: " + result.getTimetoken());
                            }
                            System.out.println("pub status code: " + status.getStatusCode()+"   error =="+status.getErrorData());
                        }
                    });
        } catch (PubnubConfigurationException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        } catch (PubnubNullException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
    }

    public void subscribeWithPresence(List<String> channelLIst, SubscribeCallback subscribeCallback) {
        PubNub pubNub = null;
        try {
            pubNub = getPubNub();
            pubNub.addListener(subscribeCallback);
            pubNub.subscribe().channels(channelLIst).withPresence().execute();
        } catch (PubnubConfigurationException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        } catch (PubnubNullException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
    }

    public void unSubscribe(List<String> channelLIst, SubscribeCallback subscribeCallback) {
        PubNub pubNub = null;
        try {
            pubNub = getPubNub();
            pubNub.removeListener(subscribeCallback);
            pubNub.unsubscribe().channels(channelLIst).execute();
        } catch (PubnubConfigurationException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        } catch (PubnubNullException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
    }

    public void getChannelState(List<String> channels, String uuid, PNCallback<PNGetStateResult> pnCallback) {
        try {
            PubNub pubnnb = getPubNub();
            pubnnb.getPresenceState().channels(channels).uuid(uuid).async(pnCallback);
        } catch (PubnubConfigurationException e) {
            e.printStackTrace();
        } catch (PubnubNullException e) {
            e.printStackTrace();
        }
    }


}
