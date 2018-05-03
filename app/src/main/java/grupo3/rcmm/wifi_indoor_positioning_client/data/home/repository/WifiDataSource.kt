package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.AppThreadExecutor
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.trilateration.NonLinearLeastSquaresSolver
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.trilateration.TrilaterationFunction
import grupo3.rcmm.wifi_indoor_positioning_client.data.base.DataSource
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.mapper.AccessPointMeasurementsMapper
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.AccessPointMeasurement
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.service.BeaconScanService
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer

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
//                var positions: Array<DoubleArray> = arrayOf(doubleArrayOf(0.toDouble(), 0.toDouble()), doubleArrayOf(0.toDouble(), 0.toDouble()), doubleArrayOf(0.toDouble(), 0.toDouble()))
//                var distances = doubleArrayOf(0.toDouble(), 0.toDouble(), 0.toDouble())
//                for (scanResult in wifiManager.scanResults) {
//                    when (scanResult.BSSID) {
//                        "00:24:c4:1b:5a:80" -> {
//                            positions[0][0] = 39.479076
//                            positions[0][1] = -6.341956
//                            distances[0] = Math.pow(10.toDouble(), (27.55 - (20 * Math.log10(scanResult.frequency.toDouble())) + scanResult.level) / 20)
//                        }
//                        "f2:98:9d:7f:79:57" -> {
//                            positions[1][0] = 39.479049
//                            positions[1][1] = -6.341970
//                            distances[1] = Math.pow(10.toDouble(), (27.55 - (20 * Math.log10(scanResult.frequency.toDouble())) + scanResult.level) / 20)
//                        }
//                        "00:24:c4:2e:97:80" -> {
//                            positions[2][0] = 39.479070
//                            positions[2][1] = -6.342007
//                            distances[2] = Math.pow(10.toDouble(), (27.55 - (20 * Math.log10(scanResult.frequency.toDouble())) + scanResult.level) / 20)
//                        }
//                    }
//                }
//                val solver = NonLinearLeastSquaresSolver(TrilaterationFunction(positions, distances), LevenbergMarquardtOptimizer())
//                val optimum = solver.solve()
//                for (item in optimum.point)
//                    Log.d("Position", "Centroide = " + item)
            }
        }
        return accessPointMeasurementsLiveData
    }
}