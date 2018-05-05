package grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.ml

/**
 * Created by victor on 5/05/18.
 */
interface MLMeasurement<T> {
    fun getValue(): T
}