package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.remote.service

import android.arch.lifecycle.LiveData
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Location
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by victor on 4/05/18.
 */
interface LocationService {

    @POST("location")
    fun postLocation(@Body location: Location): LiveData<Void>
}