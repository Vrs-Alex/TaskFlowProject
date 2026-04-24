pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Vrs-Alex/TaskFlowProject")
            credentials {
                username = providers.gradleProperty("gpr.user").getOrNull()
                password = providers.gradleProperty("gpr.key").getOrNull()
            }
        }


    }
}

rootProject.name = "TaskFlow"
include(":app")
include(":uikit")
include(":network")
