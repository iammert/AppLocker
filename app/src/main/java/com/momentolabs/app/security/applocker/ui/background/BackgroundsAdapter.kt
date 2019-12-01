package com.momentolabs.app.security.applocker.ui.background

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.databinding.ItemBackgroundGradientBinding

class BackgroundsAdapter : RecyclerView.Adapter<BackgroundsAdapter.BackgroundGradientItemViewHolder>() {

    var onItemSelected: ((item: GradientItemViewState) -> Unit)? = null

    private val gradientItemViewStateList = arrayListOf<GradientItemViewState>()

    fun setViewStateList(gradientViewStateList: List<GradientItemViewState>) {
        this.gradientItemViewStateList.clear()
        this.gradientItemViewStateList.addAll(gradientViewStateList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BackgroundGradientItemViewHolder.create(parent, onItemSelected)

    override fun getItemCount(): Int = gradientItemViewStateList.size

    override fun onBindViewHolder(holder: BackgroundGradientItemViewHolder, position: Int) =
        holder.bind(gradientItemViewStateList[position])

    class BackgroundGradientItemViewHolder(
        val binding: ItemBackgroundGradientBinding,
        private val onItemSelected: ((item: GradientItemViewState) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onItemSelected?.invoke(binding.viewState!!) }
        }

        fun bind(gradientItemViewState: GradientItemViewState) {
            binding.viewState = gradientItemViewState
            binding.executePendingBindings()
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onItemSelected: ((item: GradientItemViewState) -> Unit)?
            ): BackgroundGradientItemViewHolder {
                val binding: ItemBackgroundGradientBinding = DataBindingUtil
                    .inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_background_gradient,
                        parent,
                        false
                    )
                return BackgroundGradientItemViewHolder(binding, onItemSelected)
            }
        }
    }
}

