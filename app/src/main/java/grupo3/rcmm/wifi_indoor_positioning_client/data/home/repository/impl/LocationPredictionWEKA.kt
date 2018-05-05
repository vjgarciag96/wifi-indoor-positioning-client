package grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.impl

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import grupo3.rcmm.wifi_indoor_positioning_client.common.thread.AppThreadExecutor
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.ml.MLMeasurement
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.ml.PositionClass
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.model.ml.AccessPointAttribute
import grupo3.rcmm.wifi_indoor_positioning_client.data.home.repository.datasource.LocationPredictionDataSource
import weka.classifiers.Classifier
import weka.core.Attribute
import weka.core.DenseInstance
import weka.core.Instances
import weka.core.SerializationHelper
import java.util.*


/**
 * Created by victor on 5/05/18.
 */
class LocationPredictionWEKA(val context: Context) : LocationPredictionDataSource<Double> {

    private var classLatLng: MutableMap<String, LatLng> = mutableMapOf()

    private var attributes: ArrayList<Attribute> = arrayListOf()
    private var classes: ArrayList<String> = arrayListOf()

    private var classifier: Classifier

    init {
        val attributesFileName = "attributes.config"
        val classesFileName = "classes.config"

        val attributesConfig = context.assets.open(attributesFileName).bufferedReader().use {
            it.readText()
        }
        val classesConfig = context.assets.open(classesFileName).bufferedReader().use {
            it.readText()
        }

        val gson = GsonBuilder().create()
        val attributeType = object : TypeToken<List<AccessPointAttribute>>() {}.type
        val classType = object : TypeToken<List<PositionClass>>() {}.type

        val wifiAttributes: List<AccessPointAttribute> = gson.fromJson<List<AccessPointAttribute>>(attributesConfig, attributeType)
        val positionClasses: List<PositionClass> = gson.fromJson<List<PositionClass>>(classesConfig, classType)

        for (attribute in wifiAttributes) {
            attributes.add(Attribute(attribute.attributeName))
        }
        for (positionClass in positionClasses) {
            classes.add(positionClass.id)
            classLatLng.put(positionClass.id, LatLng(positionClass.lat, positionClass.lng))
        }
        attributes.add(Attribute("class", classes))

        classifier = SerializationHelper.read(context.assets.open("rcmmHome.model")) as Classifier
    }

    override fun getLocationPrediction(measurements: Map<String, MLMeasurement<Double>>): LiveData<LatLng> {
        val locationPrediction: MutableLiveData<LatLng> = MutableLiveData()
        AppThreadExecutor.instance.diskIO()!!.execute {
            val dataUnpredicted = Instances("instances", attributes, 1)
            dataUnpredicted.setClassIndex(dataUnpredicted.numAttributes() - 1)

            val denseInstance = DenseInstance(dataUnpredicted.numAttributes())
            for (attribute in attributes) {
                if (measurements.containsKey(attribute.name()))
                    denseInstance.setValue(attribute, measurements[attribute.name()]!!.getValue())
                else
                    denseInstance.setValue(attribute, 100.toDouble())
            }
            denseInstance.setDataset(dataUnpredicted)
            val result = classifier.classifyInstance(denseInstance)
            val className = classes.get(result.toInt())
            val position = classLatLng.get(className)
            locationPrediction.postValue(position)
        }
        return locationPrediction
    }

}