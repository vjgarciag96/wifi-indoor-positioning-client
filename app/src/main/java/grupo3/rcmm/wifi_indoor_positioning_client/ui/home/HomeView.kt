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
    fun moveMapCameraTo(location: LatLng)
    fun drawWaypoints(waypoints: List<Waypoint>, draggable: Boolean)
    fun setMarkerClickListener()
    fun removeMarkerClickListener()
    fun setMarkerDragListener()
    fun removeMarkerDragListener()
    fun addMarker(title: String, position: LatLng, draggable: Boolean)
    fun deleteMarker(marker: Marker)
    fun setUserPosition(position: LatLng)
    fun showToast(message: String)
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
    fun disableMapToolbar()
    fun disableMapCompass()
}