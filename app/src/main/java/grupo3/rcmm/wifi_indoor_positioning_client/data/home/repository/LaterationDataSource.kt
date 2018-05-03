package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.AppThreadExecutor
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.SingletonHolder
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.trilateration.NonLinearLeastSquaresSolver
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.trilateration.TrilaterationFunction
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.LaterationDevice
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.LaterationMeasurement
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer

/**
 * Created by victor on 3/05/18.
 */
class LaterationDataSource private constructor(context: Context) {

    private var positionMap: MutableMap<String, Int> = mutableMapOf()
    private var positionLiveData: MutableLiveData<LatLng> = MutableLiveData<LatLng>()

    var positions: Array<DoubleArray> = arrayOf()
    var distances: DoubleArray = doubleArrayOf()

    private var context: Context

    init {
        this.context = context
    }

    companion object : SingletonHolder<LaterationDataSource, Context>(::LaterationDataSource)

    fun getLaterationDevices(): LiveData<List<LaterationDevice>> {
        val fileName = "devices.config"
        val devicesConfig = context.assets.open(fileName).bufferedReader().use {
            it.readText()
        }
        val devicesLiveData: MutableLiveData<List<LaterationDevice>> = MutableLiveData()
        AppThreadExecutor.instance.diskIO()!!.execute {
            val gson = GsonBuilder().create()
            val laterationDevicesType = object : TypeToken<List<LaterationDevice>>() {}.type
            val devices = gson.fromJson<List<LaterationDevice>>(devicesConfig, laterationDevicesType)
            for (device in devices) {
                positionMap.put(device.mac, positions.size)
                positions[positions.size][0] = device.lat
                positions[positions.size][1] = device.lng
                distances[positions.size] = 0.toDouble()
            }
            AppThreadExecutor.instance.mainThread()!!.execute {
                devicesLiveData.value = devices
            }
        }
        return devicesLiveData
    }

    fun addLaterationMeasurement(laterationMeasurement: LaterationMeasurement) {
        var position = -1
        if (positionMap.containsKey(laterationMeasurement.mac))
            position = positionMap.get(laterationMeasurement.mac)!!

        if (position >= 0)
            distances[position] = laterationMeasurement.distance

        var ready = true
        for (distance in distances) {
            if (distance <= 0) {
                ready = false
                break
            }
        }
        if (ready) {
            val solver = NonLinearLeastSquaresSolver(TrilaterationFunction(positions, distances), LevenbergMarquardtOptimizer())
            val optimum = solver.solve()
            val pointArray = optimum.point.toArray()
            positionLiveData.value = LatLng(pointArray[0], pointArray[1])
        }
    }

    fun getPosition(): LiveData<LatLng> {
        return positionLiveData
    }

}