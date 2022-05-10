package com.apps.freeroadingdriver.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.utils.FragmentFactory
import kotlinx.android.synthetic.main.arrow_header_layout.view.*
import org.greenrobot.eventbus.Subscribe

/**
 * A simple [Fragment] subclass.
 */
class WrongEmailFragment : BaseFragment() {

    val TAG = WrongEmailFragment::class.java.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v : View = inflater!!.inflate(R.layout.fragment_wrong_email, container, false)
        v.backPress.setOnClickListener(View.OnClickListener {
            FragmentFactory.removedBack(context)
        })
        return v
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {

    }

}// Required empty public constructor
