package grupo3.rcmm.wifi_indoor_positioning_client

import android.net.wifi.ScanResult

/**
 * Created by victor on 19/04/18.
 */
data class AccessPointsEvent(val accessPoints: List<ScanResult>)