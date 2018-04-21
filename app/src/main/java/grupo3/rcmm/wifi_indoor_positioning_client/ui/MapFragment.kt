package grupo3.rcmm.wifi_indoor_positioning_client.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import grupo3.rcmm.wifi_indoor_positioning_client.R

/**
 * Created by victor on 21/04/18.
 */
class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var mapView: View

    companion object {
        fun newInstance(): Fragment = MapFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {
            this.map = map
            map.addMarker(MarkerOptions()
                    .position(LatLng(39.478896, -6.34246)))
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(39.478896, -6.34246), 100f))
        }
    }
}