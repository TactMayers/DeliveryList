package com.example.deliverylist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deliverylist.R
import com.example.deliverylist.model.DeliveryItem

/**
 * Adapter for Delivery List
 */
class DeliveryListAdapter(
    private val dataSet: List<DeliveryItem>,
    private val onClickListener: OnClickListener? = null,
    private val onBottomHitListener: OnBottomHitListener? = null
) : RecyclerView.Adapter<DeliveryListAdapter.BaseViewHolder>() {

    enum class VIEW_TYPE { NORMAL, FOOTER_LOADING, FOOTER_END }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)

    /**
     * View Holder class for a "normal" item. Shows image, description and
     * address
     */
    class NormalRowViewHolder(val view: View) : BaseViewHolder(view) {
        var ivItem: ImageView? = null
        var tvDescription: TextView? = null
        var tvAddress: TextView? = null

        init {
            ivItem = view.findViewById(R.id.iv_item)
            tvDescription = view.findViewById(R.id.tv_description)
            tvAddress = view.findViewById(R.id.tv_address)
        }
    }

    /**
     * "Loading" view holder. Indicates next page is being load and used for
     * triggering load next page event.
     */
    class LoadingRowViewHolder(view: View) : BaseViewHolder(view) {
        var tvMessage: TextView? = null

        init {
            tvMessage = view.findViewById(R.id.tv_message)
            tvMessage?.setText(R.string.text_loading_more)
        }
    }

    /**
     * "End of list" view holder. Indicates end of list is reached.
     */
    class EndOfListRowHolder(view: View) : BaseViewHolder(view) {
        var tvMessage: TextView? = null

        init {
            tvMessage = view.findViewById(R.id.tv_message)
            tvMessage?.setText(R.string.text_end_of_list)
        }
    }

    /**
     * Use for returning data of clicked item
     */
    interface OnClickListener {
        fun onItemClick(item: DeliveryItem)
    }

    /**
     * Event that triggers when bottom of the current recyclerview is reached.
     * Used for triggering loading next page
     */
    interface OnBottomHitListener {
        fun onBottomHit()
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataSet[position].id) {
            Int.MAX_VALUE - 1 -> VIEW_TYPE.FOOTER_LOADING.ordinal
            Int.MAX_VALUE -> VIEW_TYPE.FOOTER_END.ordinal
            else -> VIEW_TYPE.NORMAL.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE.FOOTER_LOADING.ordinal -> LoadingRowViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_delivery_list_footer,
                    parent,
                    false
                )
            )
            VIEW_TYPE.FOOTER_END.ordinal -> EndOfListRowHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_delivery_list_footer,
                    parent,
                    false
                )
            )
            else -> NormalRowViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_delivery_list,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is NormalRowViewHolder) {
            dataSet[position].apply {
                holder.ivItem?.let {
                    Glide.with(it).load(imageUrl).into(it)
                }
                holder.tvDescription?.text = description
                val context = holder.tvAddress?.context
                holder.tvAddress?.text = context?.getString(
                    R.string.text_delivery_list_address, location.address)
            }
            holder.view.setOnClickListener {
                onClickListener?.let {
                    it.onItemClick(dataSet[position])
                }
            }
        }
    }

    override fun getItemCount() = dataSet.size

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        // If and only if "loading" view holder is reached, trigger
        // bottom hit event. Prevent repeatedly trigger this event when
        // "End of list" is reached.
        if (holder is LoadingRowViewHolder) {
            onBottomHitListener?.onBottomHit()
        }
        super.onViewAttachedToWindow(holder)
    }
}