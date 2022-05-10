package com.apps.freeroadingdriver.activities

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.utils.DialogUtil
import com.apps.freeroadingdriver.utils.FragmentFactory


import org.greenrobot.eventbus.EventBus

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
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
        try {
            when (eventObject.id) {
                EventConstant.NETWORK_ERROR -> DialogUtil.showOkCancelDialog(
                    this,
                    getString(R.string.error),
                    getString(R.string.no_internet_connection),
                    object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                        override fun onPositiveButtonClick() {

                        }

                        override fun onNegativeButtonClick() {

                        }
                    })
                EventConstant.TOKEN_EXPIRE -> DialogUtil.showOkButtonDialog(
                    this,
                    getString(R.string.error),
                    getString(R.string.msg_session_expired)
                ) {
                    //                            UserManager.getInstance().logout();
                    //                            startActivity(LoginActivity.createIntent(BaseActivity.this));
                    finish()
                }
            }//DialogUtil.showOkButtonDialog(this, getString(R.string.error), getString(R.string.no_internet_connection));
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun changeFragmentWithTag(fragment: Fragment, tag: String) {
        FragmentFactory.replaceFragmentWithAnim(fragment, R.id.fragment_container, this, tag);
    }

    fun changeFragment(fragment: Fragment, tag: String) {
        FragmentFactory.replaceFragmentWithAnim(fragment, R.id.fragment_container, this, tag);
    }

    fun addFragment(fragment: Fragment, tag: String) {
        FragmentFactory.addFragmentWithAnim(fragment, R.id.fragment_container, this, tag);
    }

    fun changeFragmentwithoutAnimation(fragment: Fragment, tag: String) {
        FragmentFactory.replaceFragment(fragment, R.id.fragment_container, this, tag);
    }

    override fun finish() {
        super.finish()
        //        overridePendingTransitionExit();
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
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransitionExit()
    }

    companion object {
        var TAG = BaseActivity::class.java.name
    }
}
