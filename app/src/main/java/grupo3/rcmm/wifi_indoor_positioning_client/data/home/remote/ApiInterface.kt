package grupo3.rcmm.wifi_indoor_positioning_client.data.home.remote

import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Fingerprint
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    @POST("fingerprinting")
    @FormUrlEncoded
    fun postFingerprintings(@Field("info") lista: Call<List<Fingerprint>>)

    fun postLocation(@Field("pos") pos: Pair<Float, Float>)

}

