package com.momentolabs.app.security.applocker.ui.intruders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.databinding.ItemIntrudersPhotoBinding
import javax.inject.Inject

class IntrudersListAdapter @Inject constructor() :
    RecyclerView.Adapter<IntrudersListAdapter.IntrudersListItemViewHolder>() {

    private val intruderList = arrayListOf<IntruderPhotoItemViewState>()

    fun updateIntruderList(intruderList: List<IntruderPhotoItemViewState>) {
        this.intruderList.clear()
        this.intruderList.addAll(intruderList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = intruderList.size

    override fun onBindViewHolder(holder: IntrudersListItemViewHolder, position: Int) =
        holder.bind(intruderList[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntrudersListItemViewHolder =
        IntrudersListItemViewHolder.create(parent)

    class IntrudersListItemViewHolder(private val binding: ItemIntrudersPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.squareLayoutItem.setOnClickListener {

            }
        }

        fun bind(intruderPhotoItemViewState: IntruderPhotoItemViewState) {
            binding.viewState = intruderPhotoItemViewState
            binding.executePendingBindings()
        }

        companion object {

            fun create(
                parent: ViewGroup
            ): IntrudersListItemViewHolder {
                val binding: ItemIntrudersPhotoBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_intruders_photo,
                    parent,
                    false
                )
                return IntrudersListItemViewHolder(binding)

            }
        }
    }
}