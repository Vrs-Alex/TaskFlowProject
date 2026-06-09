package com.vrsalex.taskflow.app

import android.app.Application
import com.vrsalex.network.di.networkModule
import com.vrsalex.network.public.provider.DeviceIdProvider
import com.vrsalex.taskflow.di.commonModule
import com.vrsalex.taskflow.di.databaseModule
import com.vrsalex.taskflow.di.feature.featureModules
import com.vrsalex.taskflow.domain.realtime.RealtimeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TaskFlowApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TaskFlowApp)
            modules(
                commonModule,
                networkModule,
                databaseModule
            )
            modules(featureModules)
        }
        MainScope().launch(Dispatchers.IO) {
            get<DeviceIdProvider>().getDeviceId()
        }
        get<RealtimeService>().observe()

    }

}