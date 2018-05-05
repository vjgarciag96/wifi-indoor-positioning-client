package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.datasource

import android.arch.lifecycle.LiveData
import grupo3.rcmm.wifi_indoor_positioning_client.data.base.DataSource
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Fingerprinting

/**
 * Created by victor on 30/04/18.
 */
interface FingerprintingDataSource: DataSource{
    fun sendFingerprint(fingerprint: Fingerprinting): LiveData<Boolean>
}