package com.apps.freeroadingdriver.fragments

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.LoginActivity
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.manager.UserManager
import com.apps.freeroadingdriver.utils.DialogUtil

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * A simple [Fragment] subclass.
 */
abstract class BaseFragment : Fragment() {
    var contexxt: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        super.onAttach(context)
        this.contexxt = context
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    @Subscribe
    abstract fun onEvent(eventObject: EventObject)

    fun onHandleBaseEvent(eventObject: EventObject?) {
        if (eventObject == null) {
            Log.d(TAG, "on event method called but event object is null")
            return
        }
        when (eventObject.id) {
            EventConstant.NETWORK_ERROR -> DialogUtil.showOkButtonDialog(
                contexxt,
                getString(R.string.error),
                getString(R.string.no_internet_connection),
                ""
            )
            EventConstant.TOKEN_EXPIRE -> DialogUtil.showOkButtonDialog(
                contexxt,
                getString(R.string.error),
                getString(R.string.msg_session_expired)
            ) {
                UserManager.getInstance().logout();
                startActivity(LoginActivity.createIntent(requireContext()));
                requireActivity().finish()
            }
            EventConstant.SERVER_ERROR -> DialogUtil.showOkButtonDialog(
                contexxt,
                getString(R.string.error),
                getString(R.string.msg_server_error),
                ""
            )
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    companion object {
        val TAG = BaseFragment::class.java.simpleName
    }
}
