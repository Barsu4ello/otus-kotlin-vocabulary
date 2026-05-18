# Используем образ Maven для сборки
FROM eclipse-temurin:21.0.2_13-jdk AS build
WORKDIR /app

# Копируем исходный код
COPY vocabulary-be ./vocabulary-be
COPY build-plugin ./build-plugin
COPY lesson ./lesson
COPY vocabulary-other ./vocabulary-other
# Копируем gradle код
COPY settings.gradle.kts gradle.properties gradlew ./
COPY gradle ./gradle

RUN ./gradlew :vocabulary-be:test-module:bootJar --no-daemon

# Используем образ JRE для запуска приложения
FROM eclipse-temurin:21.0.2_13-jre AS runtime
WORKDIR /app

# Копируем скомпилированный JAR файл
COPY --from=build /app/vocabulary-be/test-module/build/libs/*.jar app.jar

# Запускаем приложение, указывая путь к скомпилированному JAR
ENTRYPOINT ["java", "-jar", "app.jar"]


