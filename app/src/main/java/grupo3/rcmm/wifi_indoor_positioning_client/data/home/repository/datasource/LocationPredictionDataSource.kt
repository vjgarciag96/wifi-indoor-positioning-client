package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.datasource

import android.arch.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng
import grupo3.rcmm.wifi_indoor_positioning_client.data.base.DataSource
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.ml.MLMeasurement

/**
 * Created by victor on 5/05/18.
 */
interface LocationPredictionDataSource<T> : DataSource {
    fun getLocationPrediction(measurements: Map<String, MLMeasurement<T>>): LiveData<LatLng>
}