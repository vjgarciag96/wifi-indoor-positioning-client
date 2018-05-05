package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.impl

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.AppThreadExecutor
import grupo3.rcmm.wifi_indoor_positioning_client.data.base.DataSource
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.mapper.AccessPointMeasurementsMapper
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.AccessPointMeasurement
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by victor on 28/04/18.
 */
class WifiDataSource : DataSource {

    private var context: Context

    companion object {
        private val TAG = "WifiDataSource"
    }

    constructor(context: Context) {
        this.context = context
    }

    fun getAccessPointMeasurements(): LiveData<List<AccessPointMeasurement>> {
        val accessPointMeasurementsLiveData = MutableLiveData<List<AccessPointMeasurement>>()
        val thread = (AppThreadExecutor.instance.scheduledThread() as ScheduledThreadPoolExecutor)
        thread.scheduleWithFixedDelay(Runnable {
            Log.d(TAG, "getting access point measurements...")
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            wifiManager.startScan()
            AppThreadExecutor.instance.diskIO()?.execute {
                val scanResults = AccessPointMeasurementsMapper().mapList(wifiManager.scanResults)
                accessPointMeasurementsLiveData.postValue(scanResults)
            }
        }, 0, 2, TimeUnit.SECONDS)

        return accessPointMeasurementsLiveData
    }
}