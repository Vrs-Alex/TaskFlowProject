package com.vrsalex.network.di

import com.vrsalex.network.internal.impl.AreaApiImpl
import com.vrsalex.network.internal.impl.AuthApiImpl
import com.vrsalex.network.internal.impl.EventApiImpl
import com.vrsalex.network.internal.impl.RealtimeApiImpl
import com.vrsalex.network.internal.impl.TagApiImpl
import com.vrsalex.network.public.api.AreaApi
import com.vrsalex.network.public.api.AuthApi
import com.vrsalex.network.public.api.realtime.RealtimeApi
import com.vrsalex.network.public.api.TagApi
import com.vrsalex.network.public.api.item.EventApi
import org.koin.dsl.module

internal val apiModule = module {

    single<AuthApi> { AuthApiImpl(get()) }

    single<EventApi> { EventApiImpl(get()) }

    single<TagApi> { TagApiImpl(get()) }

    single<AreaApi> { AreaApiImpl(get()) }

    single<RealtimeApi> { RealtimeApiImpl(get()) }

}