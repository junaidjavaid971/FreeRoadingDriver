package com.apps.freeroadingdriver.activities

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.eventbus.EventObject
import kotlinx.android.synthetic.main.activity_pre_login.*
import org.greenrobot.eventbus.Subscribe
import java.util.*

class PreLoginActivity : BaseActivity(), View.OnClickListener {

    //All static field goes here
    companion object {
        var TAG = PreLoginActivity::class.java.name
    }

    //OnCreate method of activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_login)
        //setting click listener
        btnLogin.setOnClickListener(this)
        btnSignup.setOnClickListener(this)
        //setting image adapterS
    }

    //Eventbus callback result will come here
    @Subscribe
    override fun onEvent(eventObject: EventObject) {

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnLogin -> {

                startActivity(LoginActivity.createIntent(this))
            }
            R.id.btnSignup -> {
                startActivity(SignUpActivity.createIntent(this))
            }
        }
    }
}
