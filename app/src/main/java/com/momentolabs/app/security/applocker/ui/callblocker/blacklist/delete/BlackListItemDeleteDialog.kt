package com.momentolabs.app.security.applocker.ui.callblocker.blacklist.delete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.databinding.DialogBlackListItemDeleteBinding
import com.momentolabs.app.security.applocker.ui.BaseBottomSheetDialog
import com.momentolabs.app.security.applocker.util.delegate.inflate

class BlackListItemDeleteDialog : BaseBottomSheetDialog<BlackListItemDeleteViewModel>() {

    private val binding: DialogBlackListItemDeleteBinding by inflate(R.layout.dialog_black_list_item_delete)

    override fun getViewModel(): Class<BlackListItemDeleteViewModel> = BlackListItemDeleteViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding.buttonDelete.setOnClickListener {
            onDeleteClicked()
        }
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    private fun onDeleteClicked() {
        viewModel.deleteFromBlackList(arguments?.getInt(KEY_BLACK_LIST_ID, -1) ?: -1)
        dismiss()
    }

    companion object {

        private const val KEY_BLACK_LIST_ID = "KEY_BLACK_LIST_ID"

        fun newInstance(blackListId: Int): AppCompatDialogFragment {
            return BlackListItemDeleteDialog().apply {
                arguments = Bundle().apply {
                    putInt(KEY_BLACK_LIST_ID, blackListId)
                }
            }
        }
    }

}