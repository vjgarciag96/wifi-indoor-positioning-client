package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository

import android.util.Log
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Fingerprint

/**
 * Created by victor on 30/04/18.
 */
class FingerprintingMockDataSource : FingerprintingDataSource {

    companion object {
        private val TAG: String = "FingerprintingMock"
    }

    override fun sendFingerprint(fingerprint: Fingerprint) {
        Log.d(TAG, "sending fingerprint...\n"
                + fingerprint.toString())
    }

}