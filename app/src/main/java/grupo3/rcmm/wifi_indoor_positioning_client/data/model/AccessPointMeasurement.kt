package grupo3.rcmm.wifi_indoor_positioning_client.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

/**
 * Created by victor on 19/04/18.
 */
data class AccessPointMeasurement(val mac: String, val rssi: Int)