package com.vrsalex.taskflow.app

import android.app.Application
import com.vrsalex.network.di.networkModule
import com.vrsalex.taskflow.di.commonModule
import com.vrsalex.taskflow.di.storageModule
import com.vrsalex.taskflow.di.featureModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TaskFlowApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TaskFlowApp)
            modules(
                commonModule,
                storageModule,
                networkModule
            )
            modules(featureModules)
        }
    }

}