package grupo3.rcmm.wifi_indoor_positioning_client.data.home.mapper

import android.net.wifi.ScanResult
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.AccessPointMeasurement

/**
 * Created by victor on 28/04/18.
 */
class AccessPointMeasurementsMapper : BaseMapper<ScanResult, AccessPointMeasurement>() {

    override fun map(toMap: ScanResult): AccessPointMeasurement {
        return AccessPointMeasurement(toMap.BSSID, toMap.level)
    }

}