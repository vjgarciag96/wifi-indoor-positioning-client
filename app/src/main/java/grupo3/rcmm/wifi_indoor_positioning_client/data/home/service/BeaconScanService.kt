package grupo3.rcmm.wifi_indoor_positioning_client.data.home.service

import android.app.Service
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.IBinder
import android.util.Log
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.trilateration.NonLinearLeastSquaresSolver
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.trilateration.TrilaterationFunction
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.trilateration.filter.SimpleKalman
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.LaterationMeasurement
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.LaterationDataSource
import org.altbeacon.beacon.*
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer

/**
 * Created by victor on 2/05/18.
 */
class BeaconScanService : Service(), BeaconConsumer {

    companion object {
        private val TAG: String = "BeaconScanService"
    }

    lateinit var beaconManager: BeaconManager

    var positions: Array<DoubleArray> = arrayOf(doubleArrayOf(0.toDouble(), 0.toDouble()),
            doubleArrayOf(0.toDouble(), 0.toDouble()),
            doubleArrayOf(0.toDouble(), 0.toDouble()))
    var distances = doubleArrayOf(0.toDouble(),
            0.toDouble(),
            0.toDouble())

    fun Beacon.log(): String {
        return "mac = " + this.bluetoothAddress + ", \n" +
                "distance = " + this.distance + ", \n" +
                "ssid = " + this.id1
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        BeaconManager.setRssiFilterImplClass(SimpleKalman::class.java)
//        BeaconManager.setRssiFilterImplClass(ArmaRssiFilter::class.java)
        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"))
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        beaconManager.bind(this)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        beaconManager.unbind(this)
    }

    override fun onBeaconServiceConnect() {
        beaconManager.addRangeNotifier({ beacons, region ->
            for (beacon in beacons) {
                LaterationDataSource.getInstance().addLaterationMeasurement(LaterationMeasurement(beacon.distance, beacon.bluetoothAddress))
            }
        })
        beaconManager.startRangingBeaconsInRegion(Region("my-region", null, null, null))
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "Binding BeaconScanService...")
        return null
    }
}