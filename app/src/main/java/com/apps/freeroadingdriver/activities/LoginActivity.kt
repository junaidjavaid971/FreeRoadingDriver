package com.apps.freeroadingdriver.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.BaseActivity
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.fragments.LoginFragment
import com.apps.freeroadingdriver.utils.FragmentFactory
import org.greenrobot.eventbus.Subscribe

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FragmentFactory.replaceFragment(LoginFragment(), R.id.fragment_container, this@LoginActivity)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            Log.d(TAG, "create Intent method")
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return intent
        }
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {

    }
}
