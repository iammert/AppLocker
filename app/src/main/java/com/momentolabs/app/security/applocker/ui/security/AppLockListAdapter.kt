package com.momentolabs.app.security.applocker.ui.security

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.momentolabs.app.security.applocker.ui.security.viewholder.AppLockItemViewHolder
import com.momentolabs.app.security.applocker.ui.security.viewholder.HeaderViewHolder
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import androidx.recyclerview.widget.DiffUtil
import com.bugsnag.android.Bugsnag
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


class AppLockListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var appItemClicked: ((AppLockItemItemViewState) -> Unit)? = null

    private val itemViewStateList: ArrayList<AppLockItemBaseViewState> = arrayListOf()

    @SuppressLint("CheckResult")
    fun setAppDataList(itemViewStateList: List<AppLockItemBaseViewState>) {
        Single
            .create<DiffUtil.DiffResult> {
                val diffResult = DiffUtil.calculateDiff(AppLockListDiffUtil(this.itemViewStateList, itemViewStateList))
                it.onSuccess(diffResult)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    this.itemViewStateList.clear()
                    this.itemViewStateList.addAll(itemViewStateList)
                    it.dispatchUpdatesTo(this)
                },
                { error ->
                    Bugsnag.notify(error)
                    this.itemViewStateList.clear()
                    this.itemViewStateList.addAll(itemViewStateList)
                    notifyDataSetChanged()
                })
    }

    override fun getItemCount(): Int = itemViewStateList.size

    override fun getItemViewType(position: Int): Int {
        return when (itemViewStateList[position]) {
            is AppLockItemHeaderViewState -> TYPE_HEADER
            is AppLockItemItemViewState -> TYPE_APP_ITEM
            else -> throw IllegalArgumentException("No type found")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_APP_ITEM -> AppLockItemViewHolder.create(parent, appItemClicked)
            TYPE_HEADER -> HeaderViewHolder.create(parent)
            else -> throw IllegalStateException("No type found")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AppLockItemViewHolder -> holder.bind(itemViewStateList[position] as AppLockItemItemViewState)
            is HeaderViewHolder -> holder.bind(itemViewStateList[position] as AppLockItemHeaderViewState)
        }
    }

    companion object {

        private const val TYPE_HEADER = 0
        private const val TYPE_APP_ITEM = 1
    }
}