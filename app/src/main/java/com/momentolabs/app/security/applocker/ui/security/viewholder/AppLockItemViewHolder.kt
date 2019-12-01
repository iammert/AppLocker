package com.momentolabs.app.security.applocker.ui.security.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.databinding.ItemAppLockListBinding
import com.momentolabs.app.security.applocker.ui.security.AppLockItemItemViewState

class AppLockItemViewHolder(
    private val binding: ItemAppLockListBinding,
    private val appItemClicked: ((AppLockItemItemViewState) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.imageViewLock.setOnClickListener {
            appItemClicked?.invoke(binding.viewState!!)
        }
    }

    fun bind(appLockItemViewState: AppLockItemItemViewState) {
        binding.viewState = appLockItemViewState
        binding.executePendingBindings()
    }

    companion object {
        fun create(
            parent: ViewGroup,
            appItemClicked: ((AppLockItemItemViewState) -> Unit)?
        ): AppLockItemViewHolder {
            val binding = DataBindingUtil.inflate<ItemAppLockListBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_app_lock_list,
                parent,
                false
            )

            return AppLockItemViewHolder(binding, appItemClicked)
        }
    }
}