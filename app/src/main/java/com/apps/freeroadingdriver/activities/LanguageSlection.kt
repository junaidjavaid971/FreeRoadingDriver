package com.apps.freeroadingdriver.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager
import com.apps.freeroadingdriver.utils.CommonUtil
import kotlinx.android.synthetic.main.activity_language_slection.*
import java.util.*

class LanguageSlection : AppCompatActivity() {
    internal var isLogin: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_slection)
        isLogin = FreeRoadingPreferenceManager.getInstance().isLogin
        val langAdapter = ArrayAdapter<String>(
            this,
            R.layout.spinner_bigtext_items,
            R.id.textvw,
            resources.getStringArray(R.array.language_array)
        );
        spin_lang?.adapter = langAdapter
        spin_lang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val pos = spin_lang.selectedItemPosition
                if (pos != 0) {
                    var jaLocale: Locale? = null
                    if (pos == 1) {
                        jaLocale = Locale("en")
                        FreeRoadingPreferenceManager.getInstance().setLanguage("en")

                    } else if (pos == 2) {
                        jaLocale = Locale("fr")
                        FreeRoadingPreferenceManager.getInstance().setLanguage("fr")
                    } else if (pos == 3) {
                        jaLocale = Locale("es")
                        FreeRoadingPreferenceManager.getInstance().setLanguage("es")
                    }
                    CommonUtil.setLocale(jaLocale!!)
                    CommonUtil.setConfigChange(this@LanguageSlection)
                    if (!isLogin) {
                        startActivity(Intent(this@LanguageSlection, PreLoginActivity::class.java))
                        finish()
                    } else {
                        if (FreeRoadingPreferenceManager.getInstance().rideActive) {
                            if (FreeRoadingPreferenceManager.getInstance().rideRoadType) {
                                startActivity(
                                    Intent(
                                        this@LanguageSlection,
                                        DashboardActivity::class.java
                                    ).putExtra(AppConstant.IS_ROAD_TRIP, true)
                                )
                                finish()
                            } else {
                                startActivity(
                                    Intent(
                                        this@LanguageSlection,
                                        DashboardActivity::class.java
                                    )
                                )
                                finish()
                            }
                        } else {
                            startActivity(Intent(this@LanguageSlection, HomeActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }
    }
}
