# ЭТАП 1: Сборка (Build)
# Используем Gradle 8.12, чтобы соответствовать требованиям плагина Ktor 3.0.0+
FROM gradle:9.3.0-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# Собираем fat jar.
# Если у тебя в корне есть файл gradlew, лучше использовать его: RUN ./gradlew buildFatJar
RUN gradle buildFatJar --no-daemon

# ЭТАП 2: Запуск (Run)
FROM eclipse-temurin:21-jre-alpine
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*-all.jar /app/taskflow.jar

ENTRYPOINT ["java", "-XX:+UseParallelGC", "-XX:MaxRAMPercentage=75", "-jar", "/app/taskflow.jar"]