package grupo3.rcmm.wifi_indoor_positioning_client.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import grupo3.rcmm.wifi_indoor_positioning_client.data.model.AccessPointMeasurement
import grupo3.rcmm.wifi_indoor_positioning_client.data.event.AccessPointsEvent
import grupo3.rcmm.wifi_indoor_positioning_client.R
import grupo3.rcmm.wifi_indoor_positioning_client.data.service.WifiService
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_layout.*

class HomeActivity : AppCompatActivity() {

    private val TAG: String = "Home Activity"

    private val REQUEST_PERMISSION_CODE: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViewListeners()
        initNavigationDrawer()
        setBaseFragment()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            startListeningWifi()
        else {
            val accessCoarseLocationPermission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            ActivityCompat.requestPermissions(this, accessCoarseLocationPermission, REQUEST_PERMISSION_CODE)
        }
    }

    private fun setBaseFragment(){
        val baseFragment = MapFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.content_frame, baseFragment)
        transaction.addToBackStack(null)
        transaction.commit()
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
                    setTitle(getString(R.string.waypoints))
                    //TODO change UI to waypoints CRUD
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

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
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
}
