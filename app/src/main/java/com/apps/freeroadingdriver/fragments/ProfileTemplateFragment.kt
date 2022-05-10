package com.apps.freeroadingdriver.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.utils.FragmentFactory
import kotlinx.android.synthetic.main.fragment_profile_template.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileTemplateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileTemplateFragment : BaseFragment(),View.OnClickListener {
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.staticsBtn->{
                changeFragment(StatisticsFragment.newInstance("",""))
                (context as DashboardActivity).setToolbarTitle(getString(R.string.statistics))
            }
            R.id.carbtn->{
                changeFragment(CarDetailsFragment.newInstance("",""))
                (context as DashboardActivity).setToolbarTitle(getString(R.string.car_details))
            }
            R.id.driverBtn->{
                changeFragment(ProfileFragment.newInstance("",""))
                (context as DashboardActivity).setToolbarTitle(getString(R.string.edit_profile))
            }
        }
    }


    private fun changeFragment(fragment : Fragment){
        FragmentFactory.replaceFragmentWithAnim(fragment,R.id.fragment_container,context, TAG)
        if (context is DashboardActivity) {
            (context as DashboardActivity).setEnableDisableDrawer(false)
            (context as DashboardActivity).setBackVisibility(true)
        }
    }
    @Subscribe override fun onEvent(eventObject: EventObject) {

    }

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_template, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        staticsBtn.setOnClickListener(this)
        carbtn.setOnClickListener(this)
        driverBtn.setOnClickListener(this)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileTemplateFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): ProfileTemplateFragment {
            val fragment = ProfileTemplateFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
