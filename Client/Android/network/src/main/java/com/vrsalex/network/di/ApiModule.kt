package com.vrsalex.network.di

import com.vrsalex.network.internal.impl.AreaApiImpl
import com.vrsalex.network.internal.impl.AuthApiImpl
import com.vrsalex.network.internal.impl.EventApiImpl
import com.vrsalex.network.internal.impl.NoteApiImpl
import com.vrsalex.network.internal.impl.NotifyApiImpl
import com.vrsalex.network.internal.impl.RealtimeApiImpl
import com.vrsalex.network.internal.impl.TagApiImpl
import com.vrsalex.network.internal.impl.TaskApiImpl
import com.vrsalex.network.public.api.AreaApi
import com.vrsalex.network.public.api.AuthApi
import com.vrsalex.network.public.api.NotifyApi
import com.vrsalex.network.public.api.TagApi
import com.vrsalex.network.public.api.item.EventApi
import com.vrsalex.network.public.api.item.NoteApi
import com.vrsalex.network.public.api.item.TaskApi
import com.vrsalex.network.public.api.realtime.RealtimeApi
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val apiModule = module {

    single<AuthApi> { AuthApiImpl(get()) }

    single<TagApi> { TagApiImpl(get()) }

    single<AreaApi> { AreaApiImpl(get()) }

    single<NoteApi> { NoteApiImpl(get()) }

    single<EventApi> { EventApiImpl(get()) }

    single<TaskApi> { TaskApiImpl(get()) }

    single<NotifyApi> { NotifyApiImpl(get()) }

    single<RealtimeApi> { RealtimeApiImpl(get(), get(named("websocketUrl"))) }

}