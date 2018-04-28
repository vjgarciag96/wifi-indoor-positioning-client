package grupo3.rcmm.wifi_indoor_positioning_client.ui.base

/**
 * Created by victor on 28/04/18.
 */
interface IPresenter<in V: BaseView> {
    fun onAttach(view: V)
    fun onDetach()
}