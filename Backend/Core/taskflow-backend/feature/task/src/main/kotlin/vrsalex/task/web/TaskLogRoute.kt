package vrsalex.task.web

import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import vrsalex.core.routing.AppRoute
import vrsalex.core.routing.protected
import vrsalex.core.sync.syncRoute
import vrsalex.shared.api.item.task.TaskLogCreateRequest
import vrsalex.shared.api.item.task.TaskLogDto
import vrsalex.shared.api.item.task.TaskLogUpdateRequest
import vrsalex.task.domain.TaskLog
import vrsalex.task.domain.TaskLogCreate
import vrsalex.task.domain.TaskLogService
import vrsalex.task.domain.TaskLogUpdate

class TaskLogRoute : AppRoute {

    override fun Route.registerRoutes() {
        val service by inject<TaskLogService>()

        protected {
            syncRoute<TaskLog, TaskLogCreate, TaskLogUpdate,
                    TaskLogCreateRequest, TaskLogUpdateRequest, TaskLogDto>(
                path = "/task-logs",
                service = service,
                toCreateDomain = { it.toDomain() },
                toUpdateDomain = { it.toDomain() },
                toResponseDto = { it.toDto() }
            )

            

        }
    }
}
