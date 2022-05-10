package com.apps.freeroadingdriver.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.apps.freeroadingdriver.FreeRoadingApp
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.fragments.SignupFragment.Companion.REQUEST_CHECK_SETTINGS
import com.apps.freeroadingdriver.manager.LocationManagerWIthGps
import com.apps.freeroadingdriver.permissions.PermissionRequest
import com.apps.freeroadingdriver.permissions.PermissionRequestHandler
import com.apps.freeroadingdriver.permissions.PermissionsUtil
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import org.greenrobot.eventbus.Subscribe
import java.lang.Exception

class SplashActivity : SplashBaseActivity(), PermissionRequest.RequestCustomPermissionGroup,
    LocationManagerWIthGps.RequestLocationListener {

    internal var isLogin: Boolean = false
    override fun onAllCustomPermissionGroupGranted() {
        LocationManagerWIthGps.getInstance().requestLocation(this@SplashActivity, this)
    }

    override fun onCustomPermissionGroupDenied() {
        DialogUtil.showOkCancelDialog(
            this,
            getString(R.string.permission),
            getString(R.string.phone_permission_rationale),
            object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                override fun onPositiveButtonClick() {
                    PermissionRequestHandler.requestCustomPermissionGroup(
                        this@SplashActivity,
                        "",
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                }

                override fun onNegativeButtonClick() {
                    finish()
                }
            })
    }

    override fun onLocationSuccess(location: Location?) {
        LocationManagerWIthGps.getInstance().stopLocationUpdate()
        permissionGrantedThenGotoNextActivity()
    }

    override fun onProviderNotFound() {
        permissionGrantedThenGotoNextActivity()
    }

    override fun onLocationFailure() {
        permissionGrantedThenGotoNextActivity()
    }

    //All static field goes here
    companion object {
        var TAG = SplashActivity::class.java.name
    }

    //OnCreate method of activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        PermissionRequestHandler.requestCustomPermissionGroup(
            this@SplashActivity,
            "",
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        isLogin = FreeRoadingPreferenceManager.getInstance().isLogin

    }

    //Eventbus callback result will come here
    @Subscribe
    override fun onEvent(eventObject: EventObject) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onPermission result method")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> LocationManagerWIthGps.getInstance()
                    .requestLocation(this@SplashActivity, this)
                Activity.RESULT_CANCELED -> finish()
                else -> {
                }
            }
        }
    }

    private fun permissionGrantedThenGotoNextActivity() {
        Log.d(TAG, "phone permission granted")
        try {
            if (FreeRoadingPreferenceManager.getInstance().getDeviceID() == null) {
                val deviceID = CommonUtil.getDeviceID(this)
                FreeRoadingPreferenceManager.getInstance().setDeviceId(deviceID)
                FreeRoadingApp.getInstance().deviceID = deviceID
            } else {
                FreeRoadingApp.getInstance().deviceID =
                    FreeRoadingPreferenceManager.getInstance().deviceID
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        goToNextActivity()
    }

    fun goToNextActivity() {
        Handler().postDelayed(Runnable {
            startActivity(Intent(this@SplashActivity, LanguageSlection::class.java))
            finish()

            /*    if (!isLogin) {
                    startActivity(Intent(this@SplashActivity, PreLoginActivity::class.java))
                    finish()
                } else {
                    if (FreeRoadingPreferenceManager.getInstance().rideActive) {
                        if (FreeRoadingPreferenceManager.getInstance().rideRoadType ){
                            startActivity(Intent(this@SplashActivity, DashboardActivity::class.java).putExtra(AppConstant.IS_ROAD_TRIP,true))
                            finish()
                        }else {
                            startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
                            finish()
                        }
                    }else{
                        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                        finish()
                    }
                }*/
        }, 4000)
    }

}
