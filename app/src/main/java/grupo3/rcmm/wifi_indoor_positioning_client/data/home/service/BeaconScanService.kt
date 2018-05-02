package grupo3.rcmm.wifi_indoor_positioning_client.data.home.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.trilateration.NonLinearLeastSquaresSolver
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.trilateration.TrilaterationFunction
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

    fun Beacon.log(): String {
        return "mac = " + this.bluetoothAddress + ", \n" +
                "distance = " + this.distance
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
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
            var positions: Array<DoubleArray> = arrayOf(doubleArrayOf(0.toDouble(), 0.toDouble()), doubleArrayOf(0.toDouble(), 0.toDouble()), doubleArrayOf(0.toDouble(), 0.toDouble()))
            var distances = doubleArrayOf(0.toDouble(), 0.toDouble(), 0.toDouble())
            for (beacon in beacons) {
                Log.d(TAG, beacon.log())
                when (beacon.bluetoothAddress) {
                    "E7:84:D0:9A:3F:60" -> {//Yellow beacon
                        positions[0][0] = 39.47826293
                        positions[0][1] = -6.34195849
                        distances[0] = beacon.distance
                    }
                    "C5:E4:91:E4:48:E6" -> {//Pink beacon
                        positions[1][0] = 39.47824327
                        positions[1][1] = -6.34199806
                        distances[1] = beacon.distance
                    }
                    "F4:34:85:6E:88:C8" -> {//Purple beacon
                        positions[2][0] = 39.47832970
                        positions[2][1] = -6.34196587
                        distances[2] = beacon.distance
                    }
                }
            }
            val solver = NonLinearLeastSquaresSolver(TrilaterationFunction(positions, distances), LevenbergMarquardtOptimizer())
            val optimum = solver.solve()
            Log.d(TAG, "Centroide = " + optimum.point.toArray())
        })
        beaconManager.startRangingBeaconsInRegion(Region("my-region", null, null, null))
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "Binding BeaconScanService...")
        return null
    }
}