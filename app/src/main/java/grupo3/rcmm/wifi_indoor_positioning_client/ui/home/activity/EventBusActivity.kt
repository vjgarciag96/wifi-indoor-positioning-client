package grupo3.rcmm.wifi_indoor_positioning_client.ui.home.activity

import android.support.v7.app.AppCompatActivity
import org.greenrobot.eventbus.EventBus

/**
 * Created by victor on 27/04/18.
 */
abstract class EventBusActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }
}