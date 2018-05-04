package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository

import android.arch.lifecycle.LiveData
import grupo3.rcmm.wifi_indoor_positioning_client.data.base.DataSource
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.AccessPointMeasurement
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Fingerprint
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Waypoint

/**
 * Created by victor on 30/04/18.
 */
interface HomeDataSource : DataSource {

    fun getAccessPointMeasurements(): LiveData<List<AccessPointMeasurement>>

    fun getWaypoints(): LiveData<List<Waypoint>>
    fun addWaypoint(waypoint: Waypoint): LiveData<Long>
    fun deleteWaypoint(id: Long)
    fun updateWaypoint(waypoint: Waypoint)

    fun addFingerprint(fingerprint: Fingerprint): LiveData<Boolean>
}