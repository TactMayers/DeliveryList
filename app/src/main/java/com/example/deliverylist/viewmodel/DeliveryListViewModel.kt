package com.example.deliverylist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.deliverylist.dao.DeliveryItemRepository
import com.example.deliverylist.dao.DeliveryRoomDatabase
import com.example.deliverylist.model.DeliveryItem
import com.example.deliverylist.model.Location
import com.example.deliverylist.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DeliveryListViewModel(application: Application) : AndroidViewModel(application) {

    private val ITEMS_PER_PAGE = 20

    private val dao = DeliveryRoomDatabase.getDatabase(application).deliveryItemDao()
    private val deliveryItemRepository = DeliveryItemRepository(dao)
    private val deliveryListFromRepo = deliveryItemRepository.repoDeliveryItems
    private val deliveryList: LiveData<Resource<List<DeliveryItem>>>
    private var isLoading = false

    val adapterList = ArrayList<DeliveryItem>()
    private var currentOffset = MutableLiveData<Int>()
    private val FOOTER_ITEM_LOADING = DeliveryItem(Int.MAX_VALUE - 1, "", "", Location("", "", ""))
    private val FOOTER_ITEM_END = DeliveryItem(Int.MAX_VALUE, "", "", Location("", "", ""))

    init {
        deliveryList = Transformations.switchMap(deliveryListFromRepo) { originalList ->
            // Observe repository's LiveData, update offset, then chain
            // the LiveData to one exposed to UI
            val newList = MutableLiveData<Resource<List<DeliveryItem>>>()
            if (originalList.isSuccess) {
                currentOffset.value?.let {
                    currentOffset.value = originalList.data.size + it
                }
            } else {
            }
            newList.value = originalList
            newList
        }
        currentOffset.value = 0
    }

    /**
     * LiveData exposed to delivery list fragment.
     */
    fun getDeliveryList(): LiveData<Resource<List<DeliveryItem>>> {
        return deliveryList
    }

    /**
     * Load items from repository.
     */
    fun loadDeliveryList(offset: Int = 0, limit: Int = ITEMS_PER_PAGE) {
        currentOffset.value = offset
        if (!isLoading) {
            GlobalScope.launch(Dispatchers.IO) {
                isLoading = true
                deliveryItemRepository.loadDeliveryItems(offset, limit)
                isLoading = false
            }
        }
    }

    /**
     * For swipe down to refresh. Delete all cached items and retrieve
     * first page from network.
     */
    fun refreshDeliveryList() {
        adapterList.removeAll { true }
        GlobalScope.launch(Dispatchers.IO) {
            deliveryItemRepository.deleteAllDeliveryItems()
        }
        loadDeliveryList()
    }

    fun loadNextDeliveryList(limit: Int = ITEMS_PER_PAGE) {
        currentOffset.value?.let { offset ->
            loadDeliveryList(offset, limit)
        }
    }

    /**
     * Add retrieved items to UI list, also handles insertion of
     * appropriate footer.
     */
    fun appendDeliveryList(partList: List<DeliveryItem>) {
        if (adapterList.size > 0) {
            // Temporary remove footer first
            val lastIndex = adapterList.size - 1
            if (adapterList[lastIndex].id > Int.MAX_VALUE - 2) {
                adapterList.removeAt(lastIndex)
            }
        }

        adapterList.addAll(partList)

        // Add back correct footer
        if (partList.isEmpty()) {
            adapterList.add(FOOTER_ITEM_END)
        } else {
            adapterList.add(FOOTER_ITEM_LOADING)
        }
    }
}
