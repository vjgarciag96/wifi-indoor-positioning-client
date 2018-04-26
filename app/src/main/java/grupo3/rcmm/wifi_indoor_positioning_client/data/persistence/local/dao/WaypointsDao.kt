package grupo3.rcmm.wifi_indoor_positioning_client.data.persistence.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import grupo3.rcmm.wifi_indoor_positioning_client.data.model.Waypoint

/**
 * Created by victor on 25/04/18.
 */
@Dao
interface WaypointsDao {

    @Insert(onConflict = REPLACE)
    fun insert(waypoint: Waypoint)

    @Insert(onConflict = REPLACE)
    fun insertAll(waypoints: List<Waypoint>)

    @Query("SELECT * from waypoints")
    fun getAll(): List<Waypoint>

    @Query("DELETE from waypoints")
    fun deleteAll()
}