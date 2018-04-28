package grupo3.rcmm.wifi_indoor_positioning_client.data.home.mapper

/**
 * Created by victor on 28/04/18.
 */
abstract class BaseMapper<O, T> {

    abstract fun map(toMap: O): T

    fun mapList(toMap: List<O>): List<T> {
        var mappedItems: MutableList<T> = mutableListOf()
        for (item in toMap)
            mappedItems.add(map(item))
        return mappedItems
    }
}