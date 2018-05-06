package grupo3.rcmm.wifi_indoor_positioning_client.ui.home

import android.Manifest
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import grupo3.rcmm.wifi_indoor_positioning_client.R
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.AppThreadExecutor
import grupo3.rcmm.wifi_indoor_positioning_client.data.base.DataManager
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Fingerprint
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Fingerprinting
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Location
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.HomeRepository
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Waypoint
import grupo3.rcmm.wifi_indoor_positioning_client.ui.base.BasePresenter
import grupo3.rcmm.wifi_indoor_positioning_client.ui.base.IPresenter
import java.util.concurrent.ScheduledThreadPoolExecutor

/**
 * Created by victor on 28/04/18.
 */
class HomePresenter<V : HomeView> : BasePresenter<V>, IPresenter<V> {

    companion object {
        private val TAG: String = "HomePresenter"
    }

    private var menuId: Int = R.id.positioning

    constructor(dataManager: DataManager) : super(dataManager)

    override fun onAttach(view: V) {
        super.onAttach(view)
        getView().loadNavigationDrawer()
        getView().loadActionBar()
        getView().createViewListeners()
        getView().loadGoogleMap()
    }

    fun onNavigationItemSelected(itemId: Int) {
        menuId = itemId
        getView().removeAllMarkers()
        getView().drawFloorPlan()
        when (itemId) {
            R.id.positioning -> {
                getView().removeMarkerClickListener()
                getView().removeMarkerDragListener()
                getView().hideAddMarkerButton()
                getView().setViewTitle(getContext().getString(R.string.positioning))
                getView().showPositioningButton()
            }
            R.id.waypoints -> {
                val getWaypointsObservable = (getDataManager() as HomeRepository).getWaypoints()
                getWaypointsObservable.observe(getView() as LifecycleOwner, Observer {
                    Log.d(TAG, "fetching " + it?.size.toString() + " waypoints")
                    getView().drawWaypoints(it!!, true)
                    //remove observers to avoid duplicate markers
                    getWaypointsObservable.removeObservers(getView() as LifecycleOwner)
                })
                getView().removeMarkerClickListener()
                getView().setMarkerDragListener()
                getView().hidePositioningButton()
                getView().setViewTitle(getContext().getString(R.string.waypoints))
                getView().showAddMarkerButton()
            }
            R.id.fingerprinting -> {
                val getWaypointsObservable = (getDataManager() as HomeRepository).getWaypoints()
                getWaypointsObservable.observe(getView() as LifecycleOwner, Observer {
                    Log.d(TAG, "fetching " + it?.size.toString() + " waypoints")
                    getView().drawWaypoints(it!!, false)
                    //remove observers to avoid duplicate markers
                    getWaypointsObservable.removeObservers(getView() as LifecycleOwner)
                })
                getView().removeMarkerDragListener()
                getView().setMarkerClickListener()
                getView().hidePositioningButton()
                getView().hideAddMarkerButton()
                getView().setViewTitle(getContext().getString(R.string.fingerprinting))
            }
        }
        getView().closeDrawerLayout()
    }

    fun onMenuOptionSelected(menuOptionId: Int) {
        when (menuOptionId) {
            android.R.id.home -> getView().openDrawerLayout()
        }
    }

    fun onMapReady() {
        getView().disableMapCompass()
        getView().disableMapToolbar()
        getView().drawFloorPlan()
        //getView().moveMapCameraTo(LatLng(39.47896890365607, -6.34215496480465))
        getView().moveMapCameraTo(LatLng(39.469202, -6.381126))
    }

    fun onAddWaypointButtonClick(position: LatLng) {
        (getDataManager() as HomeRepository)
                .addWaypoint(Waypoint(position.latitude, position.longitude))
                .observe(getView() as LifecycleOwner, Observer {
                    getView().addMarker(it.toString(), position, true)
                })
    }

    fun onPositioningButtonClicked() {
        val repository = getDataManager() as HomeRepository
        val lifecycleOwner = getView() as LifecycleOwner

        repository.getAccessPointMeasurements()
                .observe(lifecycleOwner,
                        Observer {
                            repository.getPosition(it!!)
                                    .observe(lifecycleOwner,
                                            Observer {
                                                getView().setUserPosition(it!!)
                                                repository.registerPosition(Location(it.latitude, it.longitude))
                                                        .observe(lifecycleOwner, Observer { 
                                                            when(it){
                                                                false -> getView().showToast(getContext().getString(R.string.register_position_error))
                                                            }
                                                        })
                                            })
                        })
    }

    fun onMarkerClick(marker: Marker) {
        if (menuId == R.id.fingerprinting)
            Dexter.withActivity(getView() as AppCompatActivity)
                    .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .withListener(object : PermissionListener {
                        override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                            //This permission doesn't need rationale
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse?) =
                                getView().showToast(getContext().getString(R.string.permission_denied))

                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            (getDataManager() as HomeRepository)
                                    .getAccessPointMeasurements()
                                    .observe(getView() as LifecycleOwner,
                                            Observer {
                                                Log.d(TAG, "scanned " + it?.size + " access points...")
                                                val fingerprinting: MutableList<Fingerprint> = mutableListOf()
                                                val timestamp = System.currentTimeMillis()
                                                for (measurement in it!!)
                                                    fingerprinting.add(Fingerprint(marker.position.latitude,
                                                            marker.position.longitude, measurement.rssi,
                                                            measurement.mac, timestamp))
                                                (getDataManager() as HomeRepository)
                                                        .addFingerprint(Fingerprinting(fingerprinting))
                                                        .observe(getView() as LifecycleOwner, Observer {
                                                            when (it) {
                                                                true -> getView().showToast(getContext().getString(R.string.fingerprint_success))
                                                                false -> getView().showToast(getContext().getString(R.string.fingerprint_error))
                                                            }
                                                        })
                                            })
                        }
                    }).check()
    }

    fun onMarkerDrag(position: Point, deleteButton: View) {
        if (menuId == R.id.waypoints) {
            if (overlaps(position, deleteButton))
                getView().activateDeleteButton()
            else
                getView().deactivateDeleteButton()
        }
    }

    fun onMarkerDragEnd(projectedPosition: Point, deleteButton: View,
                        marker: Marker, markerPosition: LatLng) {
        if (menuId == R.id.waypoints) {
            getView().hideDeleteButton()
            getView().deactivateDeleteButton()
            if (overlaps(projectedPosition, deleteButton)) {
                getView().deleteMarker(marker)
                (getDataManager() as HomeRepository).deleteWaypoint(marker.title.toLong())
            } else
                (getDataManager() as HomeRepository).updateWaypoint(Waypoint(marker.title.toLong(),
                        marker.position.latitude, marker.position.longitude))
        }
    }

    fun onMarkerDragStart() {
        if (menuId == R.id.waypoints)
            getView().showDeleteButton()
    }

    private fun overlaps(point: Point, view: View): Boolean {
        var viewPosition = IntArray(2)
        view.getLocationOnScreen(viewPosition)
        val overlapX: Boolean = point.x < viewPosition[0] + view.getWidth() && point.x > viewPosition[0] - view.getWidth()
        val overlapY: Boolean = point.y < viewPosition[1] + view.getHeight() && point.y > viewPosition[1] - view.getWidth()
        return overlapX && overlapY
    }
}