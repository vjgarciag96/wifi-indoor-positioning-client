package grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.ml

import com.google.gson.annotations.SerializedName

/**
 * Created by victor on 5/05/18.
 */
data class PositionClass(@SerializedName("class_id") val id: String, @SerializedName("lat") val lat: Double, @SerializedName("lng") val lng: Double)