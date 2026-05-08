package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.data.sync.OutboxHandler
import com.vrsalex.taskflow.data.sync.SyncHandler
import com.vrsalex.taskflow.data.sync.SyncRepositoryImpl
import com.vrsalex.taskflow.domain.sync.SyncUseCase
import com.vrsalex.taskflow.domain.sync.repository.SyncRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val syncModule = module {

    single<SyncRepository> { SyncRepositoryImpl(get()) }

    single { SyncHandler(get()) }

    single { OutboxHandler(CoroutineScope(SupervisorJob() + Dispatchers.Default), get()) }

    single { SyncUseCase(get(), get(), get(), get(), get(), get()) }

}