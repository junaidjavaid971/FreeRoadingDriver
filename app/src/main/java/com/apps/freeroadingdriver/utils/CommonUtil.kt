package com.apps.freeroadingdriver.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Point
import android.graphics.PorterDuff
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.telephony.TelephonyManager
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.RelativeLayout

import com.apps.freeroadingdriver.FreeRoadingApp
import com.apps.freeroadingdriver.R
import java.util.*

/**
 * Created by Atiq on 16/02/17.
 */

object CommonUtil {

    private var screenWidth = 0
    private var screenHeight = 0


    val isAndroid5: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun getScreenHeight(c: Context): Int {
        if (screenHeight == 0) {
            val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenHeight = size.y
        }

        return screenHeight
    }

    fun getScreenWidth(c: Context): Int {
        if (screenWidth == 0) {
            val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenWidth = size.x
        }

        return screenWidth
    }

    fun getDeviceID(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val tmDevice: String
        val tmSerial: String
        val androidId: String
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        tmDevice = "" + tm.deviceId
        tmSerial = "" + tm.simSerialNumber
        androidId = "" + android.provider.Settings.Secure.getString(context.contentResolver, android.provider.Settings.Secure.ANDROID_ID)

        val deviceUuid = UUID(androidId.hashCode().toLong(), tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode().toLong())
        return deviceUuid.toString()
    }

    fun showProgressBar(rlProgressBar: RelativeLayout?) {
        if (rlProgressBar != null && rlProgressBar.visibility == View.GONE) {
            rlProgressBar.visibility = View.VISIBLE
            setProgressBarColor(rlProgressBar.findViewById<View>(R.id.progress_bar) as ProgressBar)
        }
    }

    private fun setProgressBarColor(progressBar: ProgressBar) {
        if (Build.VERSION.SDK_INT <= 21) {
            progressBar.indeterminateDrawable
                    .setColorFilter(ContextCompat.getColor(FreeRoadingApp.getInstance(), R.color.colorWhite), PorterDuff.Mode.SRC_IN)
        }
    }

    fun hideProgressBar(rlProgressBar: RelativeLayout?) {
        if (rlProgressBar != null && rlProgressBar.visibility == View.VISIBLE) {
            rlProgressBar.visibility = View.GONE
        }
    }

    private var locale: Locale? = null

    fun setLocale(localeIn: Locale) {
        locale = localeIn
        if (locale != null) {
            Locale.setDefault(locale)
        }
    }

    fun setConfigChange(ctx: Context) {
        if (locale != null) {
            Locale.setDefault(locale)

            val configuration = ctx.resources.configuration
            val displayMetrics = ctx.resources.displayMetrics
            configuration.locale = locale

            ctx.resources.updateConfiguration(configuration, displayMetrics)
        }
    }
}
