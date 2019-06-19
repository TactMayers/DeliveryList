package com.example.deliverylist.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.deliverylist.viewmodel.DeliveryDetailViewModel
import com.example.deliverylist.R
import com.example.deliverylist.model.DeliveryItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

/**
 * Fragment class for delivery detail
 */
class DeliveryDetailFragment : Fragment() {

    private lateinit var deliverItem: DeliveryItem

    private lateinit var fgMap: SupportMapFragment
    private lateinit var ivItem: ImageView
    private lateinit var tvDescription: TextView
    private lateinit var tvAddress: TextView
    private lateinit var ibRecenter: ImageButton

    companion object {
        private val ARG_DELIVERY_ITEM = "delivery_item"

        fun newInstance(deliverItem: DeliveryItem): DeliveryDetailFragment {
            val fragment = DeliveryDetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_DELIVERY_ITEM, deliverItem)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: DeliveryDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            deliverItem = getParcelable(ARG_DELIVERY_ITEM)!!
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delivery_detail, container, false)
        return setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setTitle(R.string.title_delivery_detail)
            setDisplayHomeAsUpEnabled(true)
        }
        viewModel = ViewModelProviders.of(this).get(DeliveryDetailViewModel::class.java)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupView(view: View): View {
        return view.apply {
            fgMap = childFragmentManager.findFragmentById(R.id.fg_map) as SupportMapFragment
            fgMap.getMapAsync(OnDetailMapReady())
            tvDescription = view.findViewById(R.id.tv_description)
            tvDescription.text = deliverItem.description
            tvAddress = view.findViewById(R.id.tv_address)
            tvAddress.text = getString(
                R.string.text_delivery_list_address, deliverItem.location.address)
            ivItem = view.findViewById(R.id.iv_item)
            Glide.with(ivItem).load(deliverItem.imageUrl).into(ivItem)
            ibRecenter = view.findViewById(R.id.ib_recenter)
            ibRecenter.setOnClickListener(OnRecenterClickListener())
        }
    }

    inner class OnDetailMapReady : OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap?) {
            map?.let {
                deliverItem.apply {
                    viewModel.configureGoogleMap(
                        it,
                        location.lat.toDouble(),
                        location.lng.toDouble(),
                        description)
                }
            }
        }
    }

    inner class OnRecenterClickListener : View.OnClickListener {
        override fun onClick(view: View?) {
            fgMap.getMapAsync(OnDetailMapReady())
        }
    }

}
