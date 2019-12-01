package com.momentolabs.app.security.applocker.ui.vault.vaultlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaType
import com.momentolabs.app.security.applocker.databinding.FragmentVaultListBinding
import com.momentolabs.app.security.applocker.ui.BaseFragment
import com.momentolabs.app.security.applocker.ui.vault.removalconfirmationdialog.RemovalConfirmationDialog
import com.momentolabs.app.security.applocker.util.delegate.inflate

class VaultListFragment : BaseFragment<VaultListViewModel>() {

    private val binding: FragmentVaultListBinding by inflate(R.layout.fragment_vault_list)

    private val vaultListAdapter = VaultListAdapter()

    override fun getViewModel(): Class<VaultListViewModel> = VaultListViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mediaType = arguments?.getSerializable(KEY_MEDIA_TYPE) as VaultMediaType
        viewModel.setVaultMediaType(mediaType)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding.recyclerViewVaultList.adapter = vaultListAdapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getVaultListViewStateLiveData().observe(this, Observer {
            binding.viewState = it
            binding.executePendingBindings()
            vaultListAdapter.updateVaultList(it.vaultList)
        })

        vaultListAdapter.vaultMediaEntityClicked = this@VaultListFragment::onVaultMediaEntityClicked
    }

    private fun onVaultMediaEntityClicked(selectedVaultMediaEntity: VaultListItemViewState) {
        activity?.let {
            RemovalConfirmationDialog.newInstance(selectedVaultMediaEntity.vaultMediaEntity)
                .show(it.supportFragmentManager, "")
        }
    }

    companion object {

        private const val KEY_MEDIA_TYPE = "KEY_MEDIA_TYPE"

        fun newInstance(vaultMediaType: VaultMediaType): Fragment {
            return VaultListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_MEDIA_TYPE, vaultMediaType)
                }
            }
        }
    }

}