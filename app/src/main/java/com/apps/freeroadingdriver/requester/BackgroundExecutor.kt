package com.apps.freeroadingdriver.requester




import com.apps.freeroadingdriver.FreeRoadingApp
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.utils.ConnectionDetector
import org.greenrobot.eventbus.EventBus

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by Admin on 6/29/2017.
 */

class BackgroundExecutor private constructor() {

    fun execute(runnable: Runnable) {
        if (!ConnectionDetector.isConnectingToInternet(FreeRoadingApp.getInstance())) {
            EventBus.getDefault().post(EventObject(EventConstant.NETWORK_ERROR, ""))
            return
        }
        if (_exService == null) {
            _exService = Executors.newFixedThreadPool(8)
        }
        _exService!!.submit(runnable)
    }

    fun stop() {
        if (_exService != null) {
            _exService!!.shutdownNow()
            _exService = null
        }
    }

    companion object {
        private var _bgExecutor: BackgroundExecutor? = null
        private var _exService: ExecutorService? = null

        val instance: BackgroundExecutor
            get() {
                if (_bgExecutor == null) {
                    _bgExecutor = BackgroundExecutor()
                }
                if (_exService == null) {
                    _exService = Executors.newFixedThreadPool(8)
                }
                return _bgExecutor!!
            }
    }
}
