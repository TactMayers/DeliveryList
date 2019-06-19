package com.example.deliverylist.dao

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.example.deliverylist.model.DeliveryItem
import com.example.deliverylist.model.Resource
import com.example.deliverylist.network.WebServiceModel

/**
 * Repository for retrieving delivery items
 */
class DeliveryItemRepository(private val dao: DeliveryItemDao) {

    val repoDeliveryItems = MutableLiveData<Resource<List<DeliveryItem>>>()

    /**
     * Retrieve delivery items.
     * If items are available in database within given offset and limit, return
     * the items from database immediately.
     * Otherwise, attempt to retrieve the items from network API
     */
    @WorkerThread
    fun loadDeliveryItems(offset: Int, limit: Int) {
        val dataInDatabase = dao.getPartialDeliveryItems(offset, limit)
        if (dataInDatabase.isNotEmpty()) {
            repoDeliveryItems.postValue(Resource(true, 0, dataInDatabase))
        } else {
            val response = WebServiceModel.apiInterface.getDeliveryList(offset, limit).execute()
            if (response.code() == 200) {
                response.body()?.let {
                    dao.insertAll(it)
                }
                repoDeliveryItems.postValue(Resource(true, response.code(), dao.getPartialDeliveryItems(offset, limit)))
            } else {
                repoDeliveryItems.postValue(Resource(false, response.code(), ArrayList()))
            }
        }
    }

    @WorkerThread
    fun deleteAllDeliveryItems() {
        dao.deleteAll()
    }
}