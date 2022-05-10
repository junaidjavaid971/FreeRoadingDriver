package com.apps.freeroadingdriver.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.apps.freeroadingdriver.FreeRoadingApp
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.BaseActivity
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager
import kotlinx.android.synthetic.main.activity_home.*
import org.greenrobot.eventbus.Subscribe

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        local.setOnClickListener(View.OnClickListener {
            startActivity(DashboardActivity.createIntent(this))
            finish()
        })

//        Log.e("Device token",FreeRoadingPreferenceManager.getInstance().deviceToken)
        roadTrips.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,DashboardActivity::class.java)
            intent.putExtra(AppConstant.IS_ROAD_TRIP,true)
            startActivity(intent)
            finish()
        })
    }

    @Subscribe override fun onEvent(eventObject: EventObject) {

    }

}
