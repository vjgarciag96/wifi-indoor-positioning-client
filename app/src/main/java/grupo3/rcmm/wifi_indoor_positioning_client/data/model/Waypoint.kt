package grupo3.rcmm.wifi_indoor_positioning_client.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Created by victor on 26/04/18.
 */
@Entity(tableName = "waypoints")
data class Waypoint(@PrimaryKey(autoGenerate = true) val id: Long?,
                    @ColumnInfo(name = "latitude") val latitude: Double,
                    @ColumnInfo(name = "longitude") val longitude: Double){
    @Ignore
    constructor(latitude: Double, longitude: Double): this(null, latitude, longitude)
}