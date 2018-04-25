package grupo3.rcmm.wifi_indoor_positioning_client.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

/**
 * Created by victor on 19/04/18.
 */
@Entity(tableName = "fingerprinting")
data class AccessPointMeasurement(@ColumnInfo(name = "mac") val mac: String, @ColumnInfo(name = "rssi") val rssi: Int)