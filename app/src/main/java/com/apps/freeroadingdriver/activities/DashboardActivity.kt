package com.apps.freeroadingdriver.activities
import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.adapter.DrawerAdapter
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.dialogs.DeleteConfirmation
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.fragments.*
import com.apps.freeroadingdriver.interfaces.DeleteConfirmationCallBack
import com.apps.freeroadingdriver.manager.LocationManagerWIthGps
import com.apps.freeroadingdriver.manager.UserManager
import com.apps.freeroadingdriver.model.dataModel.DrawerItem
import com.apps.freeroadingdriver.model.requestModel.BaseRequest
import com.apps.freeroadingdriver.model.requestModel.ChangeAvailableRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.permissions.PermissionRequest
import com.apps.freeroadingdriver.permissions.PermissionRequestHandler
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager
import com.apps.freeroadingdriver.push.MessagingService
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.ChangeAvailablityRequester
import com.apps.freeroadingdriver.requester.LogoutRequester
import com.apps.freeroadingdriver.utils.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe
import java.util.ArrayList
class DashboardActivity : BaseActivity(), PermissionRequest.RequestCustomPermissionGroup, LocationManagerWIthGps.RequestLocationListener, DrawerAdapter.MyClickListener, DeleteConfirmationCallBack {
    override fun deleteCard() {
        logoutRequest()
    }
    override fun onDrawerItemClick(item: DrawerItem?) {
        for (drawerItem in drawerItems) {
            if (drawerItem.equals(item)) {
                drawerItem.isSelected = true
                continue
            }
            drawerItem.isSelected = false
        }
        drawerAdapter!!.notifyDataSetChanged()
        if (item!!.getName().equals(getString(R.string.menu_settings))) run {
            replaceFragmentwithAnimation(SettingFragment(), SettingFragment.TAG)
            setBackVisibility(true)
            setToolbarTitle(getString(R.string.menu_settings))
        } else if (item.name.equals(getString(R.string.logout_menu))) {
            val di = DeleteConfirmation(this, 2, this)
            di.window!!.attributes.windowAnimations = R.style.DialogAnimation //style id
            di.show()
        } else if (item.name.equals(getString(R.string.menu_ride))) run {
            if (!isRoadTrip) {
                replaceFragmentwithAnimation(RideHistoryFragment(), TAG)
            }
            else{
                replaceFragmentwithAnimation(HistoryTypeFragment(), TAG)
            }
            setBackVisibility(true)
            setToolbarTitle(getString(R.string.menu_ride))
        }else if (item.name.equals(getString(R.string.addride_menu))) run {
            isAddRideStatus = FreeRoadingPreferenceManager.getInstance().getAddRideStatus()
            if (isAddRideStatus){
                replaceFragmentwithAnimation(AddRideFragment(), TAG)
                setBackVisibility(true)
                setToolbarTitle(getString(R.string.addride_menu))
            }
            else{
                CommonMethods.showShortToast(this,"Please complete your profile.")
            }

        }else if (item.name.equals(getString(R.string.activeride_menu))) run {
           // CommonMethods.showShortToast(this,"coming soon")

             replaceFragmentwithAnimation(ActiveRideListFragment(), TAG)
             setBackVisibility(true)
             setToolbarTitle(getString(R.string.activeride_menu))
        }else if (item.name.equals(getString(R.string.ridetype_menu))) run {
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
        Handler().postDelayed({ drawer_layout.closeDrawers() }, 200)
    }
    private var drawerAdapter: DrawerAdapter? = null
    private val drawerItems = ArrayList<DrawerItem>()
    private val AVAILABLE = "1"
    private val NOT_AVAILABLE = "0"
    private var isRoadTrip: Boolean = false
    private var isAddRideStatus: Boolean = true


    companion object {
        val TAG = DashboardActivity::class.java.name

        fun createIntent(context: Context): Intent {
            Log.d(TAG, "intent created")
            val intent = Intent(context, DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    //Handlingbackpress from each and every fragment
    private fun getWhichFragmentisVisible() {
        var list: ArrayList<Fragment> = ArrayList()
        list.addAll(supportFragmentManager.fragments)
            for (frag in list) {
            if (frag is SettingFragment || frag is ActiveRideListFragment) {
                setBackVisibility(false)
                setEnableDisableDrawer(true)
//                setToolbarTitle(getString(R.string.home))
            } else if (frag is ProfileTemplateFragment) {
                setBackVisibility(true)
                setToolbarTitle(getString(R.string.settings))
            } else if (frag is ProfileFragment) {
                setBackVisibility(true)
                setToolbarTitle(getString(R.string.driver_profile))
            } else if (frag is CarDetailsFragment) {
                setBackVisibility(true)
                setToolbarTitle(getString(R.string.driver_profile))
            } else if (frag is StatisticsFragment) {
                setBackVisibility(true)
                setToolbarTitle(getString(R.string.driver_profile))
            } else if (frag is RideHIstoryDetailFragment) {
                setBackVisibility(true)
                setToolbarTitle(getString(R.string.menu_ride_history))
            } else if (frag is ResetPasswordFragment) {
                setBackVisibility(true)
                setToolbarTitle(getString(R.string.settings))
            } else if (frag is TermsConditionFragment) {
                setBackVisibility(true)
                setToolbarTitle(getString(R.string.settings))
            } else if (frag is ActiveRideDetailsFragment) {
                setBackVisibility(true)
                setToolbarTitle(getString(R.string.menu_activeride))
            }else if (frag is TripInvoiceFragment) {
                setBackVisibility(true)
                setToolbarTitle(getString(R.string.active_ride_detail))
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        PermissionRequestHandler.requestCustomPermissionGroup(this@DashboardActivity, "", Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        isRoadTrip = intent.getBooleanExtra(AppConstant.IS_ROAD_TRIP, false)

        FreeRoadingPreferenceManager.getInstance().rideRoadType=isRoadTrip;
        changeFragmentwithoutAnimation(HomeFragment.newInstance("", ""), TAG)
        setActionBarDrawerToggle()
        setDrawerAdapter()
        setHeaderView()
        onDrawerItemClick(DrawerItem(getString(R.string.home)))
        cb_availability.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                hitAvailabeApi(AVAILABLE)
            } else {
                hitAvailabeApi(NOT_AVAILABLE)
            }
            drawer_layout.closeDrawers()
        })

        iv_back.setOnClickListener(View.OnClickListener {
            FragmentFactory.back(this)
            getWhichFragmentisVisible()
        })
    }

    internal fun hitAvailabeApi(status: String) {
        if (ConnectionDetector.isConnectingToInternet(this)) {
            CommonUtil.showProgressBar(rl_progress_bar)
            BackgroundExecutor.instance.execute(ChangeAvailablityRequester(ChangeAvailableRequest(status)))
        } else {
            CommonMethods.showShortToast(this, getString(R.string.no_internet_connection))
        }
    }

    fun setHeaderView() {
        if (UserManager.getInstance().getUser() != null) {
            GlideUtil.loadImageWithDefaultImage(this, iv_profile, CommonMethods.getDriverImageUrl(UserManager.getInstance().getUser().profile_pic), R.drawable.ic_profile_placeholder)
            tv_user_name!!.setText(UserManager.getInstance().getUser().name)
        }
    }

    private fun logoutRequest() {
        CommonUtil.showProgressBar(rl_progress_bar)
        BackgroundExecutor.instance.execute(LogoutRequester(BaseRequest(false, true, false, false)))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, result: Intent?) {
        Log.d(TAG, "onActivityResult method")
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        fragment?.onActivityResult(requestCode, resultCode, result)
        super.onActivityResult(requestCode, resultCode, result)
    }

    private fun setDrawerAdapter() {
        setDrawerItemsData()
        drawerAdapter = DrawerAdapter(this@DashboardActivity, drawerItems, this)
        rv_drawer_items.setLayoutManager(GridLayoutManager(this, 1))
        rv_drawer_items.setAdapter(drawerAdapter)
    }

    private fun setDrawerItemsData() {
        if (!isRoadTrip) {
            drawerItems.add(DrawerItem(getString(R.string.menu_punch),R.drawable.ic_newcar, "Rides"))
            drawerItems.add(DrawerItem(getString(R.string.menu_ride), R.drawable.ic_ride_history,"History"))
            drawerItems.add(DrawerItem(getString(R.string.menu_settings), R.drawable.setting, ""))
            drawerItems.add(DrawerItem(getString(R.string.ridetype_menu), R.drawable.ic_newcar, "Type"))
            drawerItems.add(DrawerItem(getString(R.string.logout_menu), R.drawable.log, "out"))
        }else{
            drawerItems.add(DrawerItem(getString(R.string.menu_punch),R.drawable.ic_newcar, "Rides"))
            drawerItems.add(DrawerItem(getString(R.string.addride_menu),R.drawable.ic_newcar, "Ride"))
            drawerItems.add(DrawerItem(getString(R.string.activeride_menu),R.drawable.ic_newcar, "Ride"))
            drawerItems.add(DrawerItem("Ride",R.drawable.ic_ride_history, "History"))
            drawerItems.add(DrawerItem("Settings",R.drawable.setting, ""))
            drawerItems.add(DrawerItem(getString(R.string.logout_menu),R.drawable.ic_newcar, "Type"))
            //drawerItems.add(DrawerItem(getString(R.string.menu_ride_type), R.drawable.ic_newcar))
            drawerItems.add(DrawerItem(getString(R.string.logout_menu),R.drawable.log, "out"))
        }
    }

    private fun setActionBarDrawerToggle() {
        val actionBarToggle=object :
            ActionBarDrawerToggle(this,drawer_layout,
                toolbar,R.string.openDrawer,R.string.closeDrawer){
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }

        drawer_layout.setDrawerListener(actionBarToggle)
        actionBarToggle.syncState()
    }

    fun setToolbarTitle(title: String) {
        if (toolbar != null) {
            (toolbar!!.findViewById<TextView>(R.id.toolbar_title) as TextView).text = title
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount === 1) {  // earlier it was equal to 0
            DialogUtil.showTwoButtonDialog(this@DashboardActivity, getString(R.string.title_exit), getString(R.string.msg_exit), getString(R.string.txt_yes), getString(R.string.txt_no)
                    , object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                override fun onPositiveButtonClick() {
                    finish()
                }

                override fun onNegativeButtonClick() {

                }
            })
        } else {
            FragmentFactory.back(this)
            getWhichFragmentisVisible()

        }
    }

    //setting visibility of header according to ride request
    fun setRequestBackVisibility(visibility: Boolean) {
        if (visibility) {
            toolbar!!.visibility = View.GONE
        } else {
            toolbar!!.visibility = View.VISIBLE
        }
    }

    fun setBackVisibility(visibility: Boolean) {
        if (visibility) {
            toolbar!!.navigationIcon = null
            (toolbar!!.findViewById(R.id.iv_back) as ImageView).visibility = View.VISIBLE
            setToolbarTitle("")
        } else {
            toolbar!!.navigationIcon = resources.getDrawable(R.drawable.ic_baseline_menu_24)
            (toolbar!!.findViewById(R.id.iv_back) as ImageView).visibility = View.GONE
            setToolbarTitle(getString(R.string.punchrides));
        }
    }

    //Enabling disabling drawer
    fun setEnableDisableDrawer(flag: Boolean) {
        if (flag) {
            drawer_layout.setDrawerLockMode(DrawerLayout.STATE_IDLE)
        } else {
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    override protected fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        FragmentFactory.replaceFragmentWithAnim(HomeFragment(), R.id.container, this)
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        Log.d(TAG, "on Event method")
        if (eventObject == null) {
            return
        }
        runOnUiThread {
            try {
                CommonUtil.hideProgressBar(rl_progress_bar)
                onHandleBaseEvent(eventObject)
                var response = eventObject.`object` as BaseResponse
                when (eventObject.id) {
                    EventConstant.LOGOUT_SUCCESS -> {
                        Log.d(TAG, "on event login success")
                        openLoginActivity()
                        MessagingService.cancelNotification(this@DashboardActivity)
                        CommonMethods.showShortToast(this@DashboardActivity, response.response_msg)
                    }
                    EventConstant.LOGOUT_ERROR -> {
                        Log.d(TAG, "on event login error")
                        DialogUtil.showOkButtonDialog(this@DashboardActivity, getString(R.string.message), response.response_msg, response.response_key)

                    }
                    EventConstant.CHANGE_SUCCESS -> {
                        CommonMethods.showLongToast(this@DashboardActivity, response.response_msg)
                        if (response.change_status.equals("1")) {
                            cb_availability.setChecked(true)
                        } else {
                            cb_availability.setChecked(false)
                        }
//                        publishPubnub()
                    }
                    EventConstant.CHANGE_ERROR -> {
                        Log.d(TAG, "cancel error")
                        DialogUtil.showOkButtonDialog(this@DashboardActivity, getString(R.string.message), response.response_msg, response.response_key)
                    }
                    EventConstant.CANCEL -> {
                        FreeRoadingPreferenceManager.getInstance().setServerChannel("freeroading")
                        val intent = intent
                        finish()
                        startActivity(intent)
                    }
                    EventConstant.STRIPE_HOME_PAGE_UPDATE -> {
                        val intent = intent
                        finish()
                        startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onAllCustomPermissionGroupGranted() {
        LocationManagerWIthGps.getInstance().requestLocation(this, this)
        val fm = supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }

    override fun onCustomPermissionGroupDenied() {
        DialogUtil.showOkCancelDialog(this, getString(R.string.permission), getString(R.string.phone_permission_rationale),
                object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                    override fun onPositiveButtonClick() {
                        PermissionRequestHandler.requestCustomPermissionGroup(this@DashboardActivity, "", Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    }

                    override fun onNegativeButtonClick() {

                    }
                })
    }

    override fun onLocationSuccess(location: Location?) {
        LocationManagerWIthGps.getInstance().stopLocationUpdate()
    }

    override fun onProviderNotFound() {
    }

    override fun onLocationFailure() {
    }

    private fun replaceFragmentwithAnimation(fragment: Fragment, tag: String) {
        changeFragment(fragment, tag)
    }

    private fun addFragmentwithAnimation(fragment: Fragment, tag: String) {
        addFragment(fragment, tag)
    }

    fun openLoginActivity() {
        try {
            FreeRoadingPreferenceManager.getInstance().setAddRideStatus(true)
            startActivity(LoginActivity.createIntent(this))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
