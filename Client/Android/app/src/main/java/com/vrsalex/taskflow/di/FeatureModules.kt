package com.vrsalex.taskflow.di

import com.vrsalex.taskflow.di.feature.areaModule
import com.vrsalex.taskflow.di.feature.noteModule
import com.vrsalex.taskflow.di.feature.tagModule

val featureModules = listOf(
    areaModule,
    tagModule,
    noteModule
)