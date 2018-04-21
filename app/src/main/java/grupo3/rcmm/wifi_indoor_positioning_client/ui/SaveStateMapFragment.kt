package grupo3.rcmm.wifi_indoor_positioning_client.ui

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Created by victor on 22/04/18.
 */
class SaveStateMapFragment : SupportMapFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        if (map != null) {
            this.map = map
            map.addMarker(MarkerOptions()
                    .position(LatLng(39.478896, -6.34246)))
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(39.478896, -6.34246), 100f))
        }
    }
}