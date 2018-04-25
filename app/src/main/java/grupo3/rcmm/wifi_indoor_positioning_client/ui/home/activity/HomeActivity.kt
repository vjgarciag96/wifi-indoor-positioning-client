package grupo3.rcmm.wifi_indoor_positioning_client.ui.home.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import grupo3.rcmm.wifi_indoor_positioning_client.data.model.AccessPointMeasurement
import grupo3.rcmm.wifi_indoor_positioning_client.data.event.AccessPointsEvent
import grupo3.rcmm.wifi_indoor_positioning_client.R
import grupo3.rcmm.wifi_indoor_positioning_client.data.service.WifiService
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_layout.*
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.*
import grupo3.rcmm.wifi_indoor_positioning_client.common.MapScreen
import kotlinx.android.synthetic.main.map_layout.*


class HomeActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG: String = "Home Activity"

    private val REQUEST_PERMISSION_CODE: Int = 1

    private lateinit var map: GoogleMap

    private var currentScreen: Int = MapScreen.POSITIONING.ordinal

    private lateinit var vibrator: Vibrator

    private lateinit var deleteMarkerPosition: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViewListeners()
        initNavigationDrawer()
        loadGoogleMap()
    }

    private fun initViewListeners() {

    }

    private fun initNavigationDrawer() {
        setNavigationListeners()
        setupActionBar()
    }

    private fun setNavigationListeners() {
        home_drawer_left.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.positioning -> showPositioningView()
                R.id.waypoints -> showWaypointsView()
                R.id.fingerprinting -> showFingerprintingView()
            }
            home_drawer_layout.closeDrawers()
            true
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(appbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_nav_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                android.R.id.home -> {
                    home_drawer_layout.openDrawer(GravityCompat.START)
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadGoogleMap() {
        val mapFragment = fragmentManager
                .findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopListeningWifi()
    }

    private fun startListeningWifi() {
        val intent = Intent(this, WifiService::class.java)
        startService(intent)
    }

    private fun stopListeningWifi() {
        val intent = Intent(this, WifiService::class.java)
        stopService(intent)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onAccessPointsMeasured(apMeasurements: AccessPointsEvent) {
        val formattedMeasurements = mutableListOf<AccessPointMeasurement>()
        for (accesPoint in apMeasurements.accessPoints) {
            formattedMeasurements.add(AccessPointMeasurement(accesPoint.BSSID, accesPoint.level))
        }
        Log.d(TAG, formattedMeasurements.toString())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startListeningWifi()
                else
                    Toast.makeText(this, "Necesitas conceder permisos para utilizar la app", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        drawFloorPlan()
        map.setOnMapLongClickListener {
            if (currentScreen == MapScreen.WAYPOINTS.ordinal) {
                val mapMarker = map.addMarker(MarkerOptions()
                        .position(it))
                mapMarker.setDraggable(true)
                mapMarker.setZIndex(1000F)
            }
        }
        map.setOnMarkerClickListener {
            if (currentScreen == MapScreen.WAYPOINTS.ordinal)
                it.remove()
            true
        }
        map.setOnMarkerClickListener {
            if (currentScreen == MapScreen.FINGERPRINTING.ordinal) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    startListeningWifi()
                else {
                    val accessCoarseLocationPermission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
                    ActivityCompat.requestPermissions(this, accessCoarseLocationPermission, REQUEST_PERMISSION_CODE)
                }
            }
            true
        }
        map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker?) {
                if (currentScreen == MapScreen.WAYPOINTS.ordinal) {
                    var markerScreenPosition: Point? = null
                    if (p0 != null)
                        markerScreenPosition = map.projection.toScreenLocation(p0.position)
                    if (overlap(markerScreenPosition!!, delete_button)) {
                        delete_button.setImageResource(R.drawable.ic_delete)
                        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        vibrator.vibrate(100)
                    } else
                        delete_button.setImageResource(R.drawable.ic_delete_black)
                }
            }

            override fun onMarkerDragEnd(p0: Marker?) {
                if (currentScreen == MapScreen.WAYPOINTS.ordinal) {
                    delete_button.setVisibility(View.INVISIBLE)
                    var markerScreenPosition: Point? = null
                    if (p0 != null)
                        markerScreenPosition = map.projection.toScreenLocation(p0.position)
                    if (overlap(markerScreenPosition!!, delete_button))
                        p0!!.remove()
                    else
                        p0!!.setPosition(deleteMarkerPosition);
                    delete_button.setImageResource(R.drawable.ic_delete_black)
                }
            }

            override fun onMarkerDragStart(p0: Marker?) {
                if (currentScreen == MapScreen.WAYPOINTS.ordinal) {
                    delete_button.setVisibility(View.VISIBLE)
                    if (p0 != null)
                        deleteMarkerPosition = p0.position
                }
            }

        })
        val mapMarker = map.addMarker(MarkerOptions()
                .position(LatLng(39.478896, -6.34246)))
        mapMarker.setDraggable(true)
        mapMarker.setZIndex(1000F)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(39.478896, -6.34246), 100f))
    }

    private fun showPositioningView() {
        currentScreen = MapScreen.POSITIONING.ordinal
        setTitle(getString(R.string.positioning))
        positioning_button.visibility = View.VISIBLE
    }

    private fun hidePositioningView() {
        positioning_button.visibility = View.INVISIBLE
    }

    private fun showWaypointsView() {
        hidePositioningView()
        currentScreen = MapScreen.WAYPOINTS.ordinal
        setTitle(getString(R.string.waypoints))
    }

    private fun showFingerprintingView() {
        currentScreen = MapScreen.FINGERPRINTING.ordinal
        hidePositioningView()
        setTitle(getString(R.string.fingerprinting))
    }

    private fun overlap(point: Point, imgview: ImageView): Boolean {
        var imgCoords = IntArray(2);
        imgview.getLocationOnScreen(imgCoords);
        Log.e(TAG, " ****** Img x:" + imgCoords[0] + " y:" + imgCoords[1] + "    Point x:" + point.x + "  y:" + point.y + " Width:" + imgview.getWidth() + " Height:" + imgview.getHeight());
        val overlapX: Boolean = point.x < imgCoords[0] + imgview.getWidth() && point.x > imgCoords[0] - imgview.getWidth();
        val overlapY: Boolean = point.y < imgCoords[1] + imgview.getHeight() && point.y > imgCoords[1] - imgview.getWidth();
        return overlapX && overlapY;
    }

    private fun drawFloorPlan() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.plano)
        map.addGroundOverlay(GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromBitmap(bitmap))
                .bearing(15.837358248031318F + 180F)
                .transparency(0.5F)
                .position(LatLng(39.47896890365607, -6.34215496480465),
                        32.68545310545363f,
                        83.33159181033633f))

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(38.65563582451349, -6.376890242099763), 100f))
    }
}
