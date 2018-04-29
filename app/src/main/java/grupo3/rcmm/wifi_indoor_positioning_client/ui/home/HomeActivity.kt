package grupo3.rcmm.wifi_indoor_positioning_client.ui.home

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Vibrator
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import grupo3.rcmm.wifi_indoor_positioning_client.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_layout.*
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.*
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.HomeDataManager
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.Waypoint
import kotlinx.android.synthetic.main.map_layout.*


class HomeActivity : AppCompatActivity(), HomeView, OnMapReadyCallback {

    companion object {
        private val TAG: String = "Home Activity"
    }

    private lateinit var map: GoogleMap

    private var firstVibrator: Boolean = true
    private lateinit var vibrator: Vibrator

    private lateinit var presenter: HomePresenter<HomeView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        presenter = HomePresenter(HomeDataManager(this))
        presenter.onAttach(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null)
            presenter.onMenuOptionSelected(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    override fun loadGoogleMap() {
        val mapFragment = fragmentManager
                .findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        presenter.onMapReady()
        Log.d(TAG, "on map ready...")
    }

    override fun setMapListeners() {
        if (map != null) {
            map.setOnMapLongClickListener {
                Log.d(TAG, "map long click...")
            }
            map.setOnMarkerClickListener {
                presenter.onMarkerClick(it)
                return@setOnMarkerClickListener true
            }
            map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                override fun onMarkerDrag(p0: Marker?) =
                        presenter.onMarkerDrag(map.projection.toScreenLocation(p0!!.position), delete_button)

                override fun onMarkerDragEnd(p0: Marker?) =
                        presenter.onMarkerDragEnd(map.projection.toScreenLocation(p0!!.position), delete_button,
                                p0, p0.position)

                override fun onMarkerDragStart(p0: Marker?) = presenter.onMarkerDragStart()
            })
        }
    }

    override fun removeMapListeners() {
        if (map != null) {
            map.setOnMapLongClickListener(null)
            map.setOnMarkerClickListener(null)
            map.setOnMarkerDragListener(null)
        }
    }

    override fun drawFloorPlan() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.plano)
        map.addGroundOverlay(GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromBitmap(bitmap))
                .bearing(15.837358248031318F + 180F)
                .transparency(0.5F)
                .position(LatLng(39.47896890365607, -6.34215496480465),
                        32.68545310545363f,
                        83.33159181033633f))

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(39.47896890365607, -6.34215496480465), 19f))
    }

    override fun drawWaypoints(waypoints: List<Waypoint>, draggable: Boolean) {
        for (waypoint: Waypoint in waypoints)
            addMarker(waypoint.id.toString(), LatLng(waypoint.latitude,
                    waypoint.longitude), draggable)
    }

    override fun createViewListeners() {
        add_marker_button.setOnClickListener(View.OnClickListener {
            if (map != null)
                presenter.onAddWaypointButtonClick(map.cameraPosition.target)
        })
    }

    override fun loadNavigationDrawer() {
        home_drawer_left.setNavigationItemSelectedListener {
            presenter.onNavigationItemSelected(it.itemId)
            return@setNavigationItemSelectedListener true
        }
    }

    override fun loadActionBar() {
        setSupportActionBar(appbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_nav_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun openDrawerLayout() {
        home_drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun closeDrawerLayout() {
        home_drawer_layout.closeDrawers()
    }

    override fun addMarker(title: String,
                           position: LatLng,
                           draggable: Boolean) {
        map.addMarker(MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .anchor(0.5F, 0.5F)
                .position(position)
                .draggable(draggable)
                .title(title))
    }

    override fun deleteMarker(marker: Marker) = marker.remove()

    override fun showToast(message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun activateDeleteButton() {
        delete_button.setImageResource(R.drawable.ic_delete)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (firstVibrator)
            vibrator.vibrate(100)
        firstVibrator = false
    }

    override fun deactivateDeleteButton() {
        delete_button.setImageResource(R.drawable.ic_delete_black)
        firstVibrator = true
    }

    override fun hideDeleteButton() {
        delete_button.setVisibility(View.INVISIBLE)
    }

    override fun showDeleteButton() {
        delete_button.setVisibility(View.VISIBLE)
    }

    override fun setViewTitle(title: String) {
        setTitle(title)
    }

    override fun hideAddMarkerButton() {
        add_marker_button.visibility = View.INVISIBLE
    }

    override fun showAddMarkerButton() {
        add_marker_button.visibility = View.VISIBLE
    }

    override fun hidePositioningButton() {
        positioning_button.visibility = View.INVISIBLE
    }

    override fun showPositioningButton() {
        positioning_button.visibility = View.VISIBLE
    }

    override fun removeAllMarkers() {
        if (map != null)
            map.clear()
    }
}
