FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY gradle gradle
COPY gradlew gradlew
COPY gradle.properties gradle.properties
COPY settings.gradle.kts settings.gradle.kts
COPY build.gradle.kts build.gradle.kts
COPY app/build.gradle.kts app/build.gradle.kts
COPY core/build.gradle.kts core/build.gradle.kts
COPY core-database/build.gradle.kts core-database/build.gradle.kts
COPY shared-api/build.gradle.kts shared-api/build.gradle.kts
COPY feature/auth/build.gradle.kts feature/auth/build.gradle.kts
COPY feature/area/build.gradle.kts feature/area/build.gradle.kts
COPY feature/tag/build.gradle.kts feature/tag/build.gradle.kts
COPY feature/item/build.gradle.kts feature/item/build.gradle.kts
COPY feature/event/build.gradle.kts feature/event/build.gradle.kts
COPY feature/realtime/build.gradle.kts feature/realtime/build.gradle.kts
COPY feature/task/build.gradle.kts feature/task/build.gradle.kts

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || true

COPY . .

RUN ./gradlew :app:shadowJar --no-daemon -Dorg.gradle.jvmargs="-Xmx1g"

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/app/build/libs/*-all.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]