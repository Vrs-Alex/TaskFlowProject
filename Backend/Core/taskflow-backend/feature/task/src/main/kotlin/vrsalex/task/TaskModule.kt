package vrsalex.task

import org.koin.dsl.bind
import org.koin.dsl.module
import vrsalex.core.routing.AppRoute
import vrsalex.item.domain.conversion.SubItemConverter
import vrsalex.task.data.TaskLogR2dbcRepository
import vrsalex.task.data.TaskR2dbcRepository
import vrsalex.task.domain.*
import vrsalex.task.web.TaskLogRoute
import vrsalex.task.web.TaskRoute

val taskModule = module {

    single<TaskRepository> { TaskR2dbcRepository(get()) }
    single<TaskService> { TaskService(get(), get(), get()) }
    single { TaskConverter(get()) } bind SubItemConverter::class
    single { TaskRoute() } bind AppRoute::class

    single<TaskLogRepository> { TaskLogR2dbcRepository() }
    single<TaskLogService> { TaskLogService(get(), get(), get()) }
    single { TaskLogRoute() } bind AppRoute::class

}
