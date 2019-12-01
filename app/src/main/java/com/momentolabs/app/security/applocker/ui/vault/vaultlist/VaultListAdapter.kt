package com.momentolabs.app.security.applocker.ui.vault.vaultlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaEntity
import com.momentolabs.app.security.applocker.databinding.ItemVaultListBinding
import com.squareup.picasso.Picasso

class VaultListAdapter : RecyclerView.Adapter<VaultListAdapter.VaultListItemViewHolder>() {

    var vaultMediaEntityClicked: ((VaultListItemViewState) -> Unit)? = null

    private val vaultList = arrayListOf<VaultMediaEntity>()

    fun updateVaultList(vaultList: List<VaultMediaEntity>) {
        this.vaultList.clear()
        this.vaultList.addAll(vaultList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = vaultList.size

    override fun onBindViewHolder(holder: VaultListItemViewHolder, position: Int) = holder.bind(vaultList[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaultListItemViewHolder =
        VaultListItemViewHolder.create(parent, vaultMediaEntityClicked)

    class VaultListItemViewHolder(
        private val binding: ItemVaultListBinding,
        private val vaultMediaEntityClicked: ((VaultListItemViewState) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.squareLayoutItem.setOnClickListener {
                vaultMediaEntityClicked?.invoke(binding.viewState!!)
            }
        }

        fun bind(vaultMediaEntity: VaultMediaEntity) {
            binding.viewState = VaultListItemViewState(vaultMediaEntity)
            binding.executePendingBindings()
        }

        companion object {

            fun create(
                parent: ViewGroup,
                vaultMediaEntityClicked: ((VaultListItemViewState) -> Unit)?
            ): VaultListItemViewHolder {
                val binding: ItemVaultListBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_vault_list,
                    parent,
                    false
                )
                return VaultListItemViewHolder(binding, vaultMediaEntityClicked)

            }
        }
    }
}