package com.apps.freeroadingdriver.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.utils.DialogUtil

import org.greenrobot.eventbus.EventBus


abstract class SplashBaseActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    public override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    abstract fun onEvent(eventObject: EventObject)

    fun onHandleBaseEvent(eventObject: EventObject?) {
        if (eventObject == null) {
            Log.d(TAG, "on event method called but event object is null")
            return
        }
        when (eventObject.id) {
            EventConstant.NETWORK_ERROR -> DialogUtil.showOkCancelDialog(this, getString(R.string.error), getString(R.string.no_internet_connection), object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                override fun onPositiveButtonClick() {

                }

                override fun onNegativeButtonClick() {

                }
            })
            EventConstant.TOKEN_EXPIRE -> {
            }
        }//DialogUtil.showOkButtonDialog(this, getString(R.string.error), getString(R.string.no_internet_connection));
    }


    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        overridePendingTransitionEnter()
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected fun overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale)
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected fun overridePendingTransitionExit() {
        overridePendingTransition(R.anim.activity_close_scale, R.anim.activity_open_translate)
    }

    companion object {
        var TAG = SplashBaseActivity::class.java.name
    }
}
