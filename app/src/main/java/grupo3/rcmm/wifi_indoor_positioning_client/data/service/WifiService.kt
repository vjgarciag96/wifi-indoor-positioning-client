package grupo3.rcmm.wifi_indoor_positioning_client.data.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.IBinder
import android.util.Log
import grupo3.rcmm.wifi_indoor_positioning_client.data.event.AccessPointsEvent
import org.greenrobot.eventbus.EventBus

/**
 * Created by victor on 19/04/18.
 */
class WifiService: Service() {

    private val TAG: String = "WifiService"

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "Binding WifiService...")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getAccessPointData()
        return super.onStartCommand(intent, flags, startId)
    }

    fun getAccessPointData() {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiScanResults = wifiManager.scanResults
        EventBus.getDefault().post(AccessPointsEvent(wifiScanResults))
    }

}