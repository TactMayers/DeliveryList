package com.example.deliverylist.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.deliverylist.viewmodel.DeliveryListViewModel
import com.example.deliverylist.R
import com.example.deliverylist.adapter.DeliveryListAdapter
import com.example.deliverylist.model.DeliveryItem

/**
 * Fragment class for displaying list of delivery items
 */
class DeliveryListFragment : Fragment() {

    companion object {
        fun newInstance() = DeliveryListFragment()
    }

    private lateinit var viewModel: DeliveryListViewModel
    private lateinit var rvDeliveryList: RecyclerView
    private lateinit var slRefresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delivery_list, container, false)
        return setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setTitle(R.string.title_delivery_list)
            setDisplayHomeAsUpEnabled(false)
        }
        viewModel = ViewModelProviders.of(this).get(DeliveryListViewModel::class.java)

        setupViewModel()

        // Setup RecyclerView adapter after ViewModel init completed,
        // since data set is inside ViewModel
        rvDeliveryList.adapter = DeliveryListAdapter(
            viewModel.adapterList,
            DeliveryListItemOnClickListener(),
            OnBottomHitListener())

        // Prevent repeated loading of delivery list when back from
        // Detail Fragment
        if (viewModel.adapterList.isEmpty()) {
            slRefresh.isRefreshing = true
            viewModel.loadDeliveryList()
        }
    }

    private fun setupView(parent: View): View {
        return parent.apply {
            slRefresh = findViewById(R.id.sl_refresh)
            slRefresh.setOnRefreshListener(SwipeToRefreshListener())
            rvDeliveryList = findViewById(R.id.rv_delivery_list)
            rvDeliveryList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            rvDeliveryList.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
    }

    private fun setupViewModel() {
        viewModel.getDeliveryList().observe(viewLifecycleOwner, Observer {
            slRefresh.isRefreshing = false
            if (it.isSuccess) {
                viewModel.appendDeliveryList(it.data)
                rvDeliveryList.adapter?.notifyDataSetChanged()
            } else {
                (activity as? MainActivity)?.showSnackBarMessage(getString(R.string.text_network_error))
            }
        })
    }

    inner class DeliveryListItemOnClickListener: DeliveryListAdapter.OnClickListener {
        override fun onItemClick(item: DeliveryItem) {
            (activity as MainActivity).replaceContentWithFragment(DeliveryDetailFragment.newInstance(item))
        }
    }

    inner class SwipeToRefreshListener: SwipeRefreshLayout.OnRefreshListener {
        override fun onRefresh() {
            viewModel.refreshDeliveryList()
            rvDeliveryList.adapter?.notifyDataSetChanged()
        }
    }

    inner class OnBottomHitListener: DeliveryListAdapter.OnBottomHitListener {
        override fun onBottomHit() {
            slRefresh.isRefreshing = true
            viewModel.loadNextDeliveryList()
        }
    }
}
