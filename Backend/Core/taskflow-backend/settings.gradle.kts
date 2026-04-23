rootProject.name = "taskflow-backend"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include("app")
include("core")
include("core-database")
include("shared-api")

include("feature:auth")
include("feature:event")
include("feature:item")
include("feature:realtime")
include("feature:area")
include("feature:tag")