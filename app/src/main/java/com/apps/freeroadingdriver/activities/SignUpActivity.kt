package com.apps.freeroadingdriver.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.BaseActivity
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.fragments.BaseFragment
import com.apps.freeroadingdriver.fragments.SignupFragment
import com.apps.freeroadingdriver.manager.LocationManagerWIthGps
import com.apps.freeroadingdriver.permissions.PermissionRequest
import com.apps.freeroadingdriver.permissions.PermissionRequestHandler
import com.apps.freeroadingdriver.permissions.PermissionsUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import com.apps.freeroadingdriver.utils.FragmentFactory
import org.greenrobot.eventbus.Subscribe
class SignUpActivity : BaseActivity(), PermissionRequest.RequestCustomPermissionGroup, LocationManagerWIthGps.RequestLocationListener {

    override fun onAllCustomPermissionGroupGranted() {
        LocationManagerWIthGps.getInstance().requestLocation(this, this)
    }

    override fun onCustomPermissionGroupDenied() {
//        CommonMethods.showLongToast(this, "permission required to continue")
        DialogUtil.showOkCancelDialog(this@SignUpActivity, getString(R.string.permission), getString(R.string.phone_permission_rationale), object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
            override fun onPositiveButtonClick() {
                PermissionRequestHandler.requestCustomPermissionGroup(this@SignUpActivity, "", Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            }

            override fun onNegativeButtonClick() {
                finish()
            }
        })
    }

    override fun onLocationSuccess(location: Location?) {
        LocationManagerWIthGps.getInstance().stopLocationUpdate()
    }

    override fun onProviderNotFound() {
        CommonMethods.showLongToast(this, "provider not found")
    }

    override fun onLocationFailure() {
        CommonMethods.showLongToast(this, "unable to get location")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        PermissionRequestHandler.requestCustomPermissionGroup(this@SignUpActivity, "", Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        FragmentFactory.replaceFragment(SignupFragment(), R.id.fragment_container,this@SignUpActivity)
    }
    companion object {
        val TAG = SignUpActivity::class.java.name
        fun createIntent(context: Context): Intent {
            Log.d(TAG, "create Intent method")
            val intent = Intent(context, SignUpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return intent
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult method")
        val registerationFrag = supportFragmentManager.findFragmentById(R.id.fragment_container)
        registerationFrag?.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SignupFragment.REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> LocationManagerWIthGps.getInstance().requestLocation(this, this)
                Activity.RESULT_CANCELED ->{}
                else -> {
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.d(BaseFragment.TAG, "onRequestPermissionResult()")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    @Subscribe override fun onEvent(eventObject: EventObject) {

    }


}
