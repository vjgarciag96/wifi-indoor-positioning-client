package grupo3.rcmm.wifi_indoor_positioning_client.data.home.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import org.altbeacon.beacon.*

/**
 * Created by victor on 2/05/18.
 */
class BeaconScanService : Service(), BeaconConsumer {

    companion object {
        private val TAG: String = "BeaconScanService"
    }

    lateinit var beaconManager: BeaconManager

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
        beaconManager.addRangeNotifier(RangeNotifier { beacons, region ->
            for (beacon in beacons)
                Log.d(TAG, beacon.toString())
        })
        beaconManager.startRangingBeaconsInRegion(Region("my-region", null, null, null))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}