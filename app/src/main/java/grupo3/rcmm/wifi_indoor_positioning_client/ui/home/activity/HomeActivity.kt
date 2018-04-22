package grupo3.rcmm.wifi_indoor_positioning_client.ui.home.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
import kotlinx.android.synthetic.main.map_layout.*


class HomeActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG: String = "Home Activity"

    private val REQUEST_PERMISSION_CODE: Int = 1

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViewListeners()
        initNavigationDrawer()
        loadGoogleMap()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            startListeningWifi()
        else {
            val accessCoarseLocationPermission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            ActivityCompat.requestPermissions(this, accessCoarseLocationPermission, REQUEST_PERMISSION_CODE)
        }
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
                R.id.positioning -> {
                    setTitle(getString(R.string.positioning))
                    //TODO change UI to positioning
                }
                R.id.waypoints -> {
                    showWaypointsView()
                }
                R.id.fingerprinting -> {
                    setTitle(getString(R.string.fingerprinting))
                    //TODO change UI to fingerprinting
                }
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
        map.addMarker(MarkerOptions()
                .position(LatLng(39.478896, -6.34246)))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(39.478896, -6.34246), 100f))
    }

    private fun showWaypointsView(){
        setTitle(getString(R.string.waypoints))
        positioning_button.visibility = View.INVISIBLE
        waypoint_buttons_container.visibility = View.VISIBLE
        add_waypoint_button.setOnClickListener(View.OnClickListener {
            add_waypoint_button.setImageDrawable(getDrawable(R.drawable.ic_add_marker_red))
            delete_waypoint_button.setImageDrawable(getDrawable(R.drawable.ic_delete_marker_black))
        })
        delete_waypoint_button.setOnClickListener(View.OnClickListener {
            add_waypoint_button.setImageDrawable(getDrawable(R.drawable.ic_add_marker_black))
            delete_waypoint_button.setImageDrawable(getDrawable(R.drawable.ic_delete_marker_red))
        })
    }
}
