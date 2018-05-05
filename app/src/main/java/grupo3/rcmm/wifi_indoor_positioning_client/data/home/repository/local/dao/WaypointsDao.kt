package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Waypoint

/**
 * Created by victor on 25/04/18.
 */
@Dao
interface WaypointsDao {

    @Insert(onConflict = REPLACE)
    fun insert(waypoint: Waypoint): Long

    @Insert(onConflict = REPLACE)
    fun insertAll(waypoints: List<Waypoint>)

    @Update
    fun update(waypoint: Waypoint)

    @Query("SELECT * from waypoints")
    fun getAll(): LiveData<List<Waypoint>>

    @Query("DELETE from waypoints")
    fun deleteAll()

    @Query("DELETE from waypoints WHERE id = :id")
    fun deleteById(id: Long): Int
}