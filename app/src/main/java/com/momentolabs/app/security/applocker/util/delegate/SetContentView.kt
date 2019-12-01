package com.momentolabs.app.security.applocker.util.delegate

import android.app.Activity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.annotation.LayoutRes
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class SetContentView<in R : Activity, out DB : ViewDataBinding>(@LayoutRes private val layoutRes: Int) : ReadOnlyProperty<R, DB> {

    private var value: DB? = null

    override fun getValue(thisRef: R, property: KProperty<*>): DB {
        value = value ?: DataBindingUtil.setContentView(thisRef, layoutRes)
        return value!!
    }
}

fun <T : Activity, R : ViewDataBinding> contentView(@LayoutRes layoutRes: Int): SetContentView<T, R> {
    return SetContentView(layoutRes)
}