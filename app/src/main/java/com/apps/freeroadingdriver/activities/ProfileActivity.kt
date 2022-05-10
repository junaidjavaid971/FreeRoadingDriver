package com.apps.freeroadingdriver.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.fragments.ProfileFragment
import com.apps.freeroadingdriver.utils.FragmentFactory
import org.greenrobot.eventbus.Subscribe

class ProfileActivity : BaseActivity() {

    companion object {
        private val TAG = ProfileActivity::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        FragmentFactory.replaceFragment(
            ProfileFragment.newInstance("", ""),
            R.id.fragment_container,
            this,
            TAG
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(DashboardActivity.TAG, "onActivityResult method")
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {

    }
}
