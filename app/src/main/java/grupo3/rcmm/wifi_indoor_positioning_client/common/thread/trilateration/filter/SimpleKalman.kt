package grupo3.rcmm.wifi_indoor_positioning_client.common.thread.trilateration.filter

import org.altbeacon.beacon.service.RssiFilter

/**
 * Created by victor on 2/05/18.
 */
class SimpleKalman : RssiFilter {

    private var priorRSSI: Double = 0.toDouble()
    private var lastRSSI: Double = 0.toDouble()
    private var priorErrorCovarianceRSSI: Double = 0.toDouble()
    private var processNoise: Double = 0.toDouble()//Process noise
    private var measurementNoise: Double = 0.toDouble()//Measurement noise
    private var estimatedRSSI: Double = 0.toDouble()//calculated rssi
    private var errorCovarianceRSSI: Double = 0.toDouble()//calculated covariance
    private var isInitialized = false//initialization flag

    constructor(){
        processNoise = 0.125
        measurementNoise = 0.8
    }

    constructor(processNoise: Double, measurementNoise: Double){
        this.processNoise = processNoise
        this.measurementNoise = measurementNoise
    }


    override fun noMeasurementsAvailable(): Boolean {
        return false
    }

    override fun getMeasurementCount(): Int {
        return 0
    }

    override fun calculateRssi(): Double {
        val kalmanGain = priorErrorCovarianceRSSI / (priorErrorCovarianceRSSI + measurementNoise)

        estimatedRSSI = priorRSSI + kalmanGain * (lastRSSI - priorRSSI)
        errorCovarianceRSSI = (1 - kalmanGain) * priorErrorCovarianceRSSI

        return estimatedRSSI
    }

    override fun addMeasurement(rssi: Int?) {
        lastRSSI = rssi!!.toDouble()
        if(!isInitialized){
            priorRSSI = rssi!!.toDouble()
            priorErrorCovarianceRSSI = 1.0
            isInitialized = true
        }
        else{
            priorRSSI = estimatedRSSI
            priorErrorCovarianceRSSI = errorCovarianceRSSI + processNoise
        }
    }

}