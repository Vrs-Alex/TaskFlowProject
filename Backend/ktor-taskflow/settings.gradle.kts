rootProject.name = "ktor-taskflow"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include("app")
include("core")
include("database-core")
include("shared-api")

include("feature:account")
include("feature:workspace")
include("feature:task")
include("feature:habit")
include("feature:goal")
include("feature:event")
include("feature:reminder")
include("feature:attachment")

