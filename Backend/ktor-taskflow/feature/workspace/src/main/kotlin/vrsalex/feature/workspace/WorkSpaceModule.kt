package vrsalex.feature.workspace

import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import vrsalex.core.routing.FeatureRouter
import vrsalex.feature.workspace.data.AreaR2dbcRepository
import vrsalex.feature.workspace.data.ProjectR2dbcRepository
import vrsalex.feature.workspace.data.TagR2dbcRepository
import vrsalex.feature.workspace.domain.repository.AreaRepository
import vrsalex.feature.workspace.domain.repository.ProjectRepository
import vrsalex.feature.workspace.domain.repository.TagRepository
import vrsalex.feature.workspace.domain.service.AreaService
import vrsalex.feature.workspace.domain.service.ProjectService
import vrsalex.feature.workspace.domain.service.TagService
import vrsalex.feature.workspace.web.AreaRoute
import vrsalex.feature.workspace.web.TagRoute
import kotlin.math.sin

val workSpaceModule = module {

    single<AreaRepository> { AreaR2dbcRepository() }

    single<AreaService> { AreaService(repository = get(), get())  }


    single<TagRepository> { TagR2dbcRepository() }

    single<TagService> { TagService(get(), get()) }


    single<ProjectRepository> { ProjectR2dbcRepository() }

    single<ProjectService> { ProjectService(get(), get()) }



    single { AreaRoute() } bind FeatureRouter::class

    single { TagRoute() } bind FeatureRouter::class

}