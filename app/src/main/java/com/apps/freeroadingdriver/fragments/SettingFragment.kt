package com.apps.freeroadingdriver.fragments
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.activities.LanguageSlection
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager
import com.apps.freeroadingdriver.utils.DialogUtil
import com.apps.freeroadingdriver.utils.FragmentFactory
import kotlinx.android.synthetic.main.fragment_setting.view.*
import org.greenrobot.eventbus.Subscribe
/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : BaseFragment(), View.OnClickListener {

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
        val view = inflater!!.inflate(R.layout.fragment_setting, container, false)
        view.tv_profile_settings.setOnClickListener(this)
        view.tv_reset_password.setOnClickListener(this)
        view.tv_terms_condition.setOnClickListener(this)
        view.tv_privacy.setOnClickListener(this)
        view.tv_contact.setOnClickListener(this)
        view.tv_help.setOnClickListener(this)
        view.tv_language.setOnClickListener(this)
        return view
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        val TAG = SettingFragment::class.java.name
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): SettingFragment {
            val fragment = SettingFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {

    }
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tv_profile_settings -> {
                FragmentFactory.replaceFragmentWithAnim(ProfileTemplateFragment(), R.id.fragment_container, context, TAG)
                (context as? DashboardActivity)?.setBackVisibility(true)
                (context as? DashboardActivity)?.setToolbarTitle(getString(R.string.driver_profile))
            }
            R.id.tv_reset_password -> {
                FragmentFactory.replaceFragmentWithAnim(ResetPasswordFragment(), R.id.fragment_container, context, TAG)
                (context as? DashboardActivity)?.setBackVisibility(true)
                (context as? DashboardActivity)?.setToolbarTitle(getString(R.string.reset_password))
            }
            R.id.tv_terms_condition -> {
                FragmentFactory.replaceFragmentWithAnim(TermsConditionFragment.newInstance(AppConstant.TERMS, false), R.id.fragment_container, context, TAG)
                (context as? DashboardActivity)?.setBackVisibility(true)
                (context as? DashboardActivity)?.setToolbarTitle(getString(R.string.terms_amp_conditions))
            }
            R.id.tv_privacy -> {
                FragmentFactory.replaceFragmentWithAnim(TermsConditionFragment.newInstance(AppConstant.PRIVACY, false), R.id.fragment_container, context, TAG)
                (context as? DashboardActivity)?.setBackVisibility(true)
                (context as? DashboardActivity)?.setToolbarTitle(getString(R.string.privacy_policy))
            }
            R.id.tv_contact -> {
                FragmentFactory.replaceFragmentWithAnim(TermsConditionFragment.newInstance(AppConstant.CONTACT, false), R.id.fragment_container, context, TAG)
                (context as? DashboardActivity)?.setBackVisibility(true)
                (context as? DashboardActivity)?.setToolbarTitle(getString(R.string.contact_us))
            }
            R.id.tv_help -> {
                FragmentFactory.replaceFragmentWithAnim(TermsConditionFragment.newInstance(AppConstant.HELP, false), R.id.fragment_container, context, TAG)
                (context as? DashboardActivity)?.setBackVisibility(true)
                (context as? DashboardActivity)?.setToolbarTitle(getString(R.string.help))
            }

            R.id.tv_language -> {
                DialogUtil.showOkCancelDialog(context, getString(R.string.language), getString(R.string.change_lang), object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                    override fun onNegativeButtonClick() {

                    }

                    override fun onPositiveButtonClick() {
                        FreeRoadingPreferenceManager.getInstance().isLogin = false
                        activity!!.startActivity(Intent(context, LanguageSlection::class.java))
                        activity!!.finish()
                    }

                });
            }
        }

    }
}// Required empty public constructor
