package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.AppThreadExecutor
import grupo3.rcmm.wifi_indoor_positioning_client.data.base.DataManager
import grupo3.rcmm.wifi_indoor_positioning_client.data.base.DataSource
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.AccessPointMeasurement
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Waypoint
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.local.db.WaypointsDatabase
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Fingerprint

/**
 * Created by victor on 28/04/18.
 */
class HomeRepository(private val context: Context) : DataManager, HomeDataSource {

    private var wifiDataSource: DataSource = WifiDataSource(context)
    private var beaconDataSource: DataSource = BeaconDataSource(context)
    private var fingerprintingDataSource: DataSource = FingerprintingMockDataSource()

    override fun getAccessPointMeasurements(): LiveData<List<AccessPointMeasurement>> =
            (wifiDataSource as WifiDataSource).getAccessPointMeasurements()

    override fun getWaypoints(): LiveData<List<Waypoint>> {
        return WaypointsDatabase
                .getInstance(context)
                ?.waypointsDao()
                ?.getAll()!!
    }

    override fun addWaypoint(waypoint: Waypoint): LiveData<Long> {
        var waypointLiveData = MutableLiveData<Long>()
        AppThreadExecutor.instance.diskIO()?.execute {
            val newId = WaypointsDatabase.getInstance(context)
                    ?.waypointsDao()
                    ?.insert(waypoint)!!
            AppThreadExecutor.instance.mainThread()?.execute {
                waypointLiveData.value = newId
            }
        }
        return waypointLiveData
    }

    override fun deleteWaypoint(id: Long) {
        AppThreadExecutor.instance.diskIO()?.execute {
            WaypointsDatabase.getInstance(context)
                    ?.waypointsDao()
                    ?.deleteById(id)!!
        }
    }

    override fun updateWaypoint(waypoint: Waypoint) {
        AppThreadExecutor.instance.diskIO()?.execute {
            WaypointsDatabase.getInstance(context)
                    ?.waypointsDao()
                    ?.update(waypoint)
        }
    }

    override fun addFingerprint(fingerprint: Fingerprint) {
        (fingerprintingDataSource as FingerprintingDataSource).sendFingerprint(fingerprint)
    }

    override fun getBeaconMeasurements() {
        (beaconDataSource as BeaconDataSource).getBeaconMeasurements()
    }
}