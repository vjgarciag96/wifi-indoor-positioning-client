package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.impl

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.github.nkzawa.socketio.client.IO
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.AppThreadExecutor
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Location
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.datasource.RegisterLocationDataSource
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.remote.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterLocationAPI : RegisterLocationDataSource {

    companion object {
        private val TAG = "RegisterLocationAPI"
    }

    override fun sendLocation(location: Location): LiveData<Boolean> {
        val resultLiveData: MutableLiveData<Boolean> = MutableLiveData()
        AppThreadExecutor.instance.diskIO().execute(Runnable {
            val socket = IO.socket("http://qapps.unex.es:7779")
            if (!socket.connected())
                socket.connect()
            socket.emit("location", location.lat.toString() + "," + location.lng.toString())
        })

        APIClient.getLocationService()
                .postLocation(location)
                .enqueue(object : Callback<Void> {
                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        if (t?.message != null)
                            Log.d(TAG, t.message)
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