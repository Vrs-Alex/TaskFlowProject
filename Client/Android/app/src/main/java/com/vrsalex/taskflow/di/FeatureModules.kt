package com.vrsalex.taskflow.di

import com.vrsalex.taskflow.di.feature.areaModule
import com.vrsalex.taskflow.di.feature.authModule
import com.vrsalex.taskflow.di.feature.noteModule
import com.vrsalex.taskflow.di.feature.presentationModule
import com.vrsalex.taskflow.di.feature.syncModule
import com.vrsalex.taskflow.di.feature.tagModule

val featureModules = listOf(
    authModule,

    syncModule,
    areaModule,
    tagModule,
    noteModule,

    presentationModule
)