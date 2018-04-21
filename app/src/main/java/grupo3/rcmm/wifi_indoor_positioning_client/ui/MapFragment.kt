package grupo3.rcmm.wifi_indoor_positioning_client.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import grupo3.rcmm.wifi_indoor_positioning_client.R

/**
 * Created by victor on 21/04/18.
 */
class MapFragment: Fragment(){

    companion object {
        fun newInstance(): Fragment = MapFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, bundle: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment, viewGroup, false)
    }
}