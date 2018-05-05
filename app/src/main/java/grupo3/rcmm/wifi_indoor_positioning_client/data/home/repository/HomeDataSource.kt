package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository

import android.arch.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng
import grupo3.rcmm.wifi_indoor_positioning_client.data.base.DataSource
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.AccessPointMeasurement
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Fingerprinting
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

    fun addFingerprint(fingerprint: Fingerprinting): LiveData<Boolean>

    fun getPosition(measurements: List<AccessPointMeasurement>): LiveData<LatLng>
}