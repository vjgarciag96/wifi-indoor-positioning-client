package grupo3.rcmm.wifi_indoor_positioning_client.data.persistence.local.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import grupo3.rcmm.wifi_indoor_positioning_client.data.model.Waypoint
import grupo3.rcmm.wifi_indoor_positioning_client.data.persistence.local.dao.WaypointsDao

/**
 * Created by victor on 25/04/18.
 */
@Database(entities = arrayOf(Waypoint::class), version = 2)
abstract class WaypointsDatabase : RoomDatabase() {

    abstract fun waypointsDao(): WaypointsDao

    companion object {
        private var INSTANCE: WaypointsDatabase? = null

        fun getInstance(context: Context): WaypointsDatabase? {
            if (INSTANCE == null) {
                synchronized(WaypointsDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            WaypointsDatabase::class.java, "waypoint.db")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}