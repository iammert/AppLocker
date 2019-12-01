package com.momentolabs.app.security.applocker.ui.security

import androidx.recyclerview.widget.DiffUtil

class AppLockListDiffUtil(
    val oldList: List<AppLockItemBaseViewState>,
    val newList: List<AppLockItemBaseViewState>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if ((oldItemPosition in 0 until oldList.size).not() || (newItemPosition in 0 until newList.size).not()) {
            return false
        }

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        if (oldItem is AppLockItemItemViewState && newItem is AppLockItemItemViewState) {
            return oldItem.appData.packageName == newItem.appData.packageName
        }

        if (oldItem is AppLockItemHeaderViewState && newItem is AppLockItemHeaderViewState) {
            return oldItem.headerTextResource == newItem.headerTextResource
        }

        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        if (oldItem is AppLockItemItemViewState && newItem is AppLockItemItemViewState) {
            return oldItem.isLocked == newItem.isLocked
        }

        if (oldItem is AppLockItemHeaderViewState && newItem is AppLockItemHeaderViewState) {
            return oldItem.headerTextResource == newItem.headerTextResource
        }

        return false
    }

}