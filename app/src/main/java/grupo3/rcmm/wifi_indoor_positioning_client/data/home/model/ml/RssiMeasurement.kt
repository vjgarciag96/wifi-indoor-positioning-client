package grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.ml

/**
 * Created by victor on 5/05/18.
 */
data class RssiMeasurement(val rssi: Double) : MLMeasurement<Double> {

    override fun getValue(): Double {
        return rssi
    }

}