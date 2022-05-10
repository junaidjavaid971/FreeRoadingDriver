package com.apps.freeroadingdriver;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import androidx.multidex.MultiDexApplication;
import android.util.Log;

import com.apps.freeroadingdriver.constants.AppConstant;
import com.apps.freeroadingdriver.network.FreeApiInterface;
import com.apps.freeroadingdriver.network.URLConstant;
import com.apps.freeroadingdriver.pubnub.PubNubManager;
import com.apps.freeroadingdriver.pubnub.PubnubConfigurationException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Harshil on 11/16/2017.
 */

public class FreeRoadingApp extends MultiDexApplication {
    public static final String TAG = FreeRoadingApp.class.getName();

    private static FreeRoadingApp mInstance;
    private final ArrayList<Object> registeredManagers;
    private String deviceID;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    /**
     * Handler to execute runnable in UI thread.
     */
    private final Handler handler;
    private FreeApiInterface coreApi;
    private OkHttpClient client;

    public FreeRoadingApp() {
        mInstance = this;
        registeredManagers = new ArrayList<>();
        handler = new Handler();
    }

    public static FreeRoadingApp getInstance() {
        if (mInstance == null) {
            mInstance = new FreeRoadingApp();
        }
        return mInstance;
    }


    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate() method called");
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
        setHttpClient();
        initCoreApiRetrofit();
//        initializeDatabase();

        try { initpubnub(); } catch (PubnubConfigurationException e) { e.printStackTrace(); }
    }
    private void initpubnub() throws PubnubConfigurationException { PubNubManager.init(AppConstant.PUBNUB_SUBSCRIPTION_KEY, AppConstant.PUBNUB_PUBLISH_KEY); }
    private void setHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }


    /*private void initializeDatabase() {
        TypedArray managerClasses = getResources().obtainTypedArray(
                R.array.managers);
        for (int index = 0; index < managerClasses.length(); index++)
            try {
                Class.forName(managerClasses.getString(index));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        managerClasses.recycle();

        TypedArray tableClasses = getResources().obtainTypedArray(R.array.tables);
        for (int index = 0; index < tableClasses.length(); index++)
            try {
                Class.forName(tableClasses.getString(index));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        tableClasses.recycle();
    }*/

    /**
     * Register new manager.
     *
     * @param manager
     */
    public void addManager(Object manager) {
        registeredManagers.add(manager);
    }


    private void initCoreApiRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLConstant.Companion.getAPP_SERVER_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        coreApi = retrofit.create(FreeApiInterface.class);
    }
    public void postDelayed(Runnable runnable, long delay) {
        if (runnable != null) {
            handler.postDelayed(runnable, delay);
        }
    }

    public FreeApiInterface getCoreApi() {
        return coreApi;
    }
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}
