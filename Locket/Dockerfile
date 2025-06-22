# 1. Base image with Gradle + JDK 21
FROM gradle:8.13-jdk21 AS build

# 2. Set workdir và copy code
WORKDIR /app
COPY . .

# 3. Gán quyền thực thi cho gradlew
RUN chmod +x ./gradlew

# 4. Build project
RUN ./gradlew clean build --no-daemon

# 5. Runtime image
FROM eclipse-temurin:21-jdk
WORKDIR /app

# 6. Copy jar từ stage build
COPY --from=build /app/build/libs/*.jar Modis.jar

# 7. Run app
ENTRYPOINT ["java", "-jar", "Modis.jar"]
