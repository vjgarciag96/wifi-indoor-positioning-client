package grupo3.rcmm.wifi_indoor_positioning_client.common.thread

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by victor on 25/04/18.
 */
public class DBThreadExecutor private constructor() {

    private object Holder {
        val INSTANCE = DBThreadExecutor()
    }

    companion object {
        val instance: DBThreadExecutor by lazy { Holder.INSTANCE }
    }

    private var diskIO: Executor? = null

    init {
        diskIO = Executors.newSingleThreadExecutor()
    }

    public fun diskIO(): Executor? {
        return diskIO
    }
}