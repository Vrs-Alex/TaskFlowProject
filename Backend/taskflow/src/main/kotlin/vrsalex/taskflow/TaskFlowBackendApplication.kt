package vrsalex.taskflow

import org.flywaydb.core.Flyway
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@SpringBootApplication
class TaskFlowBackendApplication

fun main(args: Array<String>) {
	runApplication<TaskFlowBackendApplication>(*args)
}

