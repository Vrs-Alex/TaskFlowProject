package vrsalex.task.web

import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject
import vrsalex.core.routing.AppRouter
import vrsalex.core.routing.protected
import vrsalex.core.sync.syncRoute
import vrsalex.shared.api.item.task.TaskCreateRequest
import vrsalex.shared.api.item.task.TaskDto
import vrsalex.shared.api.item.task.TaskUpdateRequest
import vrsalex.task.domain.Task
import vrsalex.task.domain.TaskCreate
import vrsalex.task.domain.TaskService
import vrsalex.task.domain.TaskUpdate

class TaskRoute : AppRouter {

    override fun Route.registerRoutes() {
        val service by inject<TaskService>()

        protected {
            syncRoute<Task, TaskCreate, TaskUpdate,
                    TaskCreateRequest, TaskUpdateRequest, TaskDto>(
                path = "/tasks",
                service = service,
                toCreateDomain = { it.toDomain() },
                toUpdateDomain = { it.toDomain() },
                toResponseDto = { it.toDto() }
            )
        }
    }
}
