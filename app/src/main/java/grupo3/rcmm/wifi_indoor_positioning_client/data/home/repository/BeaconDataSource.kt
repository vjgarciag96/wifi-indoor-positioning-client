package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository

import android.content.Context
import android.content.Intent
import grupo3.rcmm.wifi_indoor_positioning_client.data.base.DataSource
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.service.BeaconScanService

/**
 * Created by victor on 2/05/18.
 */
class BeaconDataSource : DataSource {

    private var context: Context

    constructor(context: Context) {
        this.context = context
    }

    fun getBeaconMeasurements() {
        val beaconScanIntent = Intent(context, BeaconScanService::class.java)
        context.startService(beaconScanIntent)
    }
}