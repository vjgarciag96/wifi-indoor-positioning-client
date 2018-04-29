package grupo3.rcmm.wifi_indoor_positioning_client.ui.home

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import grupo3.rcmm.wifi_indoor_positioning_client.ui.base.BaseView
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Waypoint

/**
 * Created by victor on 28/04/18.
 */
interface HomeView : BaseView {
    fun loadNavigationDrawer()
    fun loadActionBar()
    fun loadGoogleMap()
    fun openDrawerLayout()
    fun closeDrawerLayout()
    fun drawFloorPlan()
    fun drawWaypoints(waypoints: List<Waypoint>)
    fun setMapListeners()
    fun removeMapListeners()
    fun addMarker(title: String, position: LatLng)
    fun deleteMarker(marker: Marker)
    fun showToast(message: Int)
    fun showDeleteButton()
    fun hideDeleteButton()
    fun activateDeleteButton()
    fun deactivateDeleteButton()
    fun createViewListeners()
    fun setViewTitle(title: String)
    fun hideAddMarkerButton()
    fun showAddMarkerButton()
    fun hidePositioningButton()
    fun showPositioningButton()
    fun removeAllMarkers()
}