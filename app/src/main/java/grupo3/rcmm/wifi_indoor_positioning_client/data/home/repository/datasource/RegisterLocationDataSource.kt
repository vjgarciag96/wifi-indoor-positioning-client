package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.datasource

import android.arch.lifecycle.LiveData
import grupo3.rcmm.wifi_indoor_positioning_client.data.base.DataSource
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Location

interface RegisterLocationDataSource: DataSource {
    fun sendLocation(location: Location): LiveData<Boolean>
}