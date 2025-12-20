package com.momentolabs.app.security.applocker

import com.facebook.soloader.SoLoader
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
        Beaver.build(this)
        ServiceStarter.startService(this)
        SoLoader.init(this, false)
        WorkerStarter.startServiceCheckerWorker(this)
    }
}