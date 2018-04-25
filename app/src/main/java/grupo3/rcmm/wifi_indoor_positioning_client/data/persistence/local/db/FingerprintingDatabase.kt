package grupo3.rcmm.wifi_indoor_positioning_client.data.persistence.local.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import grupo3.rcmm.wifi_indoor_positioning_client.data.model.AccessPointMeasurement
import grupo3.rcmm.wifi_indoor_positioning_client.data.persistence.local.dao.FingerprintingDao

/**
 * Created by victor on 25/04/18.
 */
@Database(entities = arrayOf(AccessPointMeasurement::class), version = 1)
abstract class FingerprintingDatabase : RoomDatabase() {

    abstract fun fingerprintingDao(): FingerprintingDao

    companion object {
        private var INSTANCE: FingerprintingDatabase? = null

        fun getInstance(context: Context): FingerprintingDatabase? {
            if (INSTANCE == null) {
                synchronized(FingerprintingDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            FingerprintingDatabase::class.java, "fingerprinting.db")
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