package grupo3.rcmm.wifi_indoor_positioning_client.api

import com.google.gson.Gson
import grupo3.rcmm.wifi_indoor_positioning_client.data.Fingerprint
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @POST("fingerprinting")
    @FormUrlEncoded
    fun postFingerprintings(@Field("info") lista: Call<List<Fingerprint>>)

    fun postLocation(@Field("pos") pos: Pair<Float, Float>)

}

