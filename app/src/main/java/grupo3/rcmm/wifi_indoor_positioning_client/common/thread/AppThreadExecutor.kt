package grupo3.rcmm.wifi_indoor_positioning_client.common.thread

import android.os.Handler
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import android.os.Looper
import android.support.annotation.NonNull


/**
 * Created by victor on 25/04/18.
 */
public class AppThreadExecutor private constructor() {

    private object Holder {
        val INSTANCE = AppThreadExecutor()
    }

    companion object {
        val instance: AppThreadExecutor by lazy { Holder.INSTANCE }

        private class MainThreadExecutor : Executor {
            private val mainThreadHandler = Handler(Looper.getMainLooper())

            override fun execute(@NonNull command: Runnable) {
                mainThreadHandler.post(command)
            }
        }
    }

    private var diskIO: Executor? = null
    private var mainThread: Executor? = null

    init {
        diskIO = Executors.newSingleThreadExecutor()
        mainThread = MainThreadExecutor()
    }

    fun diskIO(): Executor? {
        return diskIO
    }

    fun mainThread(): Executor? {
        return mainThread
    }
}