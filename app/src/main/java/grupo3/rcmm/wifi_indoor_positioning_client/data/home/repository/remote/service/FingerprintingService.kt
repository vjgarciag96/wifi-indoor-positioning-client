package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.remote.service

import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Fingerprinting
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FingerprintingService {
    @POST("fingerprint")
    fun postFingerprint(@Body fingerprint: Fingerprinting): Call<Void>
}
