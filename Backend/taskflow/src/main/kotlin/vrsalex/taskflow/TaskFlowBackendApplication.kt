package vrsalex.taskflow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaskFlowBackendApplication

fun main(args: Array<String>) {
	runApplication<TaskFlowBackendApplication>(*args)
}
