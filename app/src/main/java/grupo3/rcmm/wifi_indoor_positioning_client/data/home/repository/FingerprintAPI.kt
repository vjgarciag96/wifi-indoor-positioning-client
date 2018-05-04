package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Fingerprint
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.remote.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by victor on 4/05/18.
 */
class FingerprintAPI : FingerprintingDataSource {

    companion object {
        private val TAG = "FingerprintAPI"
    }

    override fun sendFingerprint(fingerprint: Fingerprint): LiveData<Boolean> {
        val resultLiveData: MutableLiveData<Boolean> = MutableLiveData()
        APIClient.getClient().postFingerprint(fingerprint).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                Log.d(TAG, t!!.message)
                resultLiveData.postValue(false)
            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                if (response != null && response.isSuccessful)
                    resultLiveData.postValue(true)
                else
                    resultLiveData.postValue(false)
            }

        })
        return resultLiveData
    }

}