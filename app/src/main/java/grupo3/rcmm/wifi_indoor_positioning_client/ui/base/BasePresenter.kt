package grupo3.rcmm.wifi_indoor_positioning_client.ui.base

import grupo3.rcmm.wifi_indoor_positioning_client.data.base.DataManager

/**
 * Created by victor on 28/04/18.
 */
abstract class BasePresenter<V: BaseView>(): IPresenter<V> {

    private lateinit var view: V

    private lateinit var dataManager: DataManager

    constructor(dataManager: DataManager) : this() {
        this.dataManager = dataManager
    }

    override fun onAttach(view: V) {
        this.view = view
    }

    override fun onDetach() {

    }

    open fun getView(): V{
        return view
    }

    fun getDataManager(): DataManager {
        return dataManager
    }

}