package grupo3.rcmm.wifi_indoor_positioning_client.data.persistence.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import grupo3.rcmm.wifi_indoor_positioning_client.data.model.AccessPointMeasurement
import grupo3.rcmm.wifi_indoor_positioning_client.data.model.Fingerprint

/**
 * Created by victor on 25/04/18.
 */
@Dao
interface FingerprintingDao {

    @Insert(onConflict = REPLACE)
    fun insert(fingerprinting: AccessPointMeasurement)

    @Query("SELECT * from fingerprinting")
    fun getAll(): List<AccessPointMeasurement>
}