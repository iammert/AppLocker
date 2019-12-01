package com.momentolabs.app.security.applocker.ui.vault.removalconfirmationdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaEntity
import com.momentolabs.app.security.applocker.databinding.DialogRemovalConfirmationBinding
import com.momentolabs.app.security.applocker.ui.vault.removingvaultdialog.RemoveFromVaultDialog
import com.momentolabs.app.security.applocker.util.delegate.inflate

class RemovalConfirmationDialog : BottomSheetDialogFragment() {

    private val binding: DialogRemovalConfirmationBinding by inflate(R.layout.dialog_removal_confirmation)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding.buttonAccept.setOnClickListener {
            arguments?.getParcelable<VaultMediaEntity>(KEY_VAULT_MEDIA_ENTITY)?.let { vaultMediaEntity ->
                activity?.let {
                    dismiss()
                    RemoveFromVaultDialog.newInstance(vaultMediaEntity).show(it.supportFragmentManager, "")
                }
            }
        }

        return binding.root
    }

    companion object {
        private const val KEY_VAULT_MEDIA_ENTITY = "KEY_VAULT_MEDIA_ENTITY"

        fun newInstance(vaultMediaEntity: VaultMediaEntity): DialogFragment {
            return RemovalConfirmationDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_VAULT_MEDIA_ENTITY, vaultMediaEntity)
                }
            }
        }
    }
}