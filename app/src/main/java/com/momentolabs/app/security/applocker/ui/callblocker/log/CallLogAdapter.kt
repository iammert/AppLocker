package com.momentolabs.app.security.applocker.ui.callblocker.log

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.databinding.ItemCallBlockerLogsBinding
import com.momentolabs.app.security.applocker.util.extensions.inflateAdapterItem

class CallLogAdapter : RecyclerView.Adapter<CallLogAdapter.CallLogItemViewHolder>() {
    private val callLogItems = arrayListOf<CallLogItemViewState>()

    fun setCallLogsItems(callLogItems: List<CallLogItemViewState>) {
        this.callLogItems.clear()
        this.callLogItems.addAll(callLogItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = callLogItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogItemViewHolder =
        CallLogItemViewHolder.create(parent)


    override fun onBindViewHolder(holder: CallLogItemViewHolder, position: Int) = holder.bind(callLogItems[position])

    class CallLogItemViewHolder(val binding: ItemCallBlockerLogsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(callLogItemViewState: CallLogItemViewState) {
            binding.viewState = callLogItemViewState
            binding.executePendingBindings()
        }

        companion object {

            fun create(parent: ViewGroup): CallLogItemViewHolder {
                return CallLogItemViewHolder(parent.inflateAdapterItem(R.layout.item_call_blocker_logs))
            }
        }

    }
}