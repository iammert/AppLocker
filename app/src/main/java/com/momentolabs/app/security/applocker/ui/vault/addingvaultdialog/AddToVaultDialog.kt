package com.momentolabs.app.security.applocker.ui.vault.addingvaultdialog

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaType
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaType.TYPE_IMAGE
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaType.TYPE_VIDEO
import com.momentolabs.app.security.applocker.databinding.DialogAddToVaultBinding
import com.momentolabs.app.security.applocker.ui.BaseBottomSheetDialog
import com.momentolabs.app.security.applocker.util.delegate.inflate

class AddToVaultDialog : BaseBottomSheetDialog<AddToVaultViewModel>() {

    var onDismissListener: (() -> Unit)? = null

    private val binding: DialogAddToVaultBinding by inflate(R.layout.dialog_add_to_vault)

    override fun getViewModel(): Class<AddToVaultViewModel> = AddToVaultViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filePath = arguments?.getStringArrayList(KEY_FILE_PATH)
        val mediaType = arguments?.getSerializable(KEY_MEDIA_TYPE) as VaultMediaType

        viewModel.getAddToVaultViewStateLiveData().observe(viewLifecycleOwner, Observer { viewState ->
            binding.viewState = viewState
            binding.executePendingBindings()

            if (viewState.processState == ProcessState.COMPLETE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && viewState.undeletedMediaUris.isNotEmpty()) {
                    val request = MediaStore.createDeleteRequest(requireContext().contentResolver, viewState.undeletedMediaUris)
                    deleteRequestLauncher.launch(IntentSenderRequest.Builder(request.intentSender).build())
                } else {
                    finish()
                }
            }
        })

        filePath?.let { viewModel.setSelectedFilePath(it, mediaType) }
    }

    // The dialog closes whatever the user decides, the media is in the vault either way.
    private val deleteRequestLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { finish() }

    private fun finish() {
        onDismissListener?.invoke()
        dismiss()
    }

    companion object {

        private const val KEY_FILE_PATH = "KEY_FILE_PATH"

        private const val KEY_MEDIA_TYPE = "KEY_MEDIA_TYPE"

        fun newInstance(selectedFilePath: ArrayList<String?>, mediaType: VaultMediaType): AddToVaultDialog {
            return AddToVaultDialog().apply {
                isCancelable = false
                arguments = Bundle().apply {
                    putStringArrayList(KEY_FILE_PATH, selectedFilePath)
                    putSerializable(KEY_MEDIA_TYPE, mediaType)
                }
            }
        }
    }
}