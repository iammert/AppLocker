package com.momentolabs.app.security.applocker.ui.background

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.databinding.FragmentBackgroundsBinding
import com.momentolabs.app.security.applocker.ui.BaseFragment
import com.momentolabs.app.security.applocker.util.delegate.inflate

class BackgroundsFragment : BaseFragment<BackgroundsFragmentViewModel>() {

    private val binding: FragmentBackgroundsBinding by inflate(R.layout.fragment_backgrounds)

    private val backgroundsAdapter = BackgroundsAdapter()

    override fun getViewModel(): Class<BackgroundsFragmentViewModel> = BackgroundsFragmentViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding.recyclerViewBackgrounds.adapter = backgroundsAdapter
        backgroundsAdapter.onItemSelected = { onBackgroundItemSelected(it) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getBackgroundViewStateLiveData().observe(viewLifecycleOwner, Observer {
            backgroundsAdapter.setViewStateList(it)
        })
    }

    private fun onBackgroundItemSelected(selectedItemViewState: GradientItemViewState) {
        viewModel.onSelectedItemChanged(selectedItemViewState)
    }

    companion object {

        fun newInstance(): Fragment = BackgroundsFragment()
    }
}