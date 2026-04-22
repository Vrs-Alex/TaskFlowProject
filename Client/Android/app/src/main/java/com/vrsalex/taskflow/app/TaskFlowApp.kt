package com.vrsalex.taskflow.app

import android.app.Application
import com.vrsalex.taskflow.di.commonModule
import com.vrsalex.taskflow.di.feature.featureModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TaskFlowApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TaskFlowApp)
            modules(
                commonModule
            )
            modules(featureModules)
        }

    }

}