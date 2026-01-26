package com.momentolabs.app.security.applocker

import com.facebook.soloader.SoLoader
import com.momentolabs.app.security.applocker.di.component.DaggerAppComponent
import com.momentolabs.app.security.applocker.service.ServiceStarter
import com.momentolabs.app.security.applocker.util.helper.file.LegacyStorageMigrator
import com.momentolabs.app.security.applocker.service.worker.WorkerStarter
import com.raqun.beaverlib.Beaver
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

class AppLockerApplication : DaggerApplication() {

    @Inject
    lateinit var legacyStorageMigrator: LegacyStorageMigrator

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().create(this)

    override fun onCreate() {
        super.onCreate()
        legacyStorageMigrator.migrateInBackground()
        Beaver.build(this)
        ServiceStarter.startService(this)
        SoLoader.init(this, false)
        WorkerStarter.startServiceCheckerWorker(this)
    }
}