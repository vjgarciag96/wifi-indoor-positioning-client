package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.net.wifi.WifiManager
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.AppThreadExecutor
import grupo3.rcmm.wifi_indoor_positioning_client.data.base.DataSource
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.mapper.AccessPointMeasurementsMapper
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.AccessPointMeasurement

/**
 * Created by victor on 28/04/18.
 */
class WifiDataSource : DataSource {

    private var wifiManager: WifiManager

    constructor(context: Context) {
        wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    fun getAccessPointMeasurements(): LiveData<List<AccessPointMeasurement>> {
        val accessPointMeasurementsLiveData = MutableLiveData<List<AccessPointMeasurement>>()
        AppThreadExecutor.instance.diskIO()?.execute {
            val scanResults = AccessPointMeasurementsMapper().mapList(wifiManager.scanResults)
            AppThreadExecutor.instance.mainThread()?.execute {
                accessPointMeasurementsLiveData.value = scanResults
            }
        }
        return accessPointMeasurementsLiveData
    }
}