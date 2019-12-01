package com.momentolabs.app.security.applocker

import android.content.Context
import androidx.multidex.MultiDex
import com.bugsnag.android.Bugsnag
import com.facebook.FacebookSdk
import com.facebook.soloader.SoLoader
import com.facebook.stetho.Stetho
import com.google.android.gms.ads.MobileAds
import com.momentolabs.app.security.applocker.di.component.DaggerAppComponent
import com.momentolabs.app.security.applocker.service.ServiceStarter
import com.momentolabs.app.security.applocker.service.worker.WorkerStarter
import com.raqun.beaverlib.Beaver
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class AppLockerApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().create(this)

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this, getString(R.string.mobile_ad_id))
        Stetho.initializeWithDefaults(this)
        Bugsnag.init(this)
        Beaver.build(this)
        ServiceStarter.startService(this)
        SoLoader.init(this, false)
        WorkerStarter.startServiceCheckerWorker()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}