package com.momentolabs.app.security.applocker.util.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun <T : ViewDataBinding> ViewGroup.inflateAdapterItem(layoutRes: Int): T = DataBindingUtil.inflate(LayoutInflater.from(this.context), layoutRes, this, false)