# 1. Base image with JDK
FROM gradle:8.4.0-jdk21 as build

# 2. Copy source code
COPY . /app
WORKDIR /app

# 3. Build the app using Gradle wrapper
RUN ./gradlew clean build --no-daemon

# 4. Use JDK to run jar
FROM openjdk:21
WORKDIR /app

# 5. Copy jar from previous stage
COPY --from=build /app/build/libs/*.jar Modis.jar

# 6. Run app
ENTRYPOINT ["java", "-jar", "Modis.jar"]
