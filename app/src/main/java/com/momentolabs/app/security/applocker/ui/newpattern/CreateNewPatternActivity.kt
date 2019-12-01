package com.momentolabs.app.security.applocker.ui.newpattern

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.andrognito.patternlockview.PatternLockView
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.databinding.ActivityCreateNewPatternBinding
import com.momentolabs.app.security.applocker.ui.BaseActivity

class CreateNewPatternActivity : BaseActivity<CreateNewPatternViewModel>() {

    private lateinit var binding: ActivityCreateNewPatternBinding

    override fun getViewModel(): Class<CreateNewPatternViewModel> = CreateNewPatternViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_new_pattern)

        binding.patternLockView.addPatternLockListener(object : SimplePatternListener() {
            override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
                if (viewModel.isFirstPattern()) {
                    viewModel.setFirstDrawedPattern(pattern)
                } else {
                    viewModel.setRedrawnPattern(pattern)
                }
                binding.patternLockView.clearPattern()
            }
        })

        viewModel.getPatternEventLiveData().observe(this, Observer { viewState ->
            binding.viewState = viewState
            binding.executePendingBindings()

            if (viewState.isCreatedNewPattern()) {
                onPatternCreateCompleted()
            }
        })
    }

    private fun onPatternCreateCompleted() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, CreateNewPatternActivity::class.java)
        }
    }
}