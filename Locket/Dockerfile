# 1. Base image with JDK
FROM gradle:8.4.0-jdk21 as build

# 2. Copy source code
COPY . /app
WORKDIR /app

# 3. Gán quyền thực thi cho gradlew (nếu cần)
RUN chmod +x ./gradlew

# 4. Build project
RUN ./gradlew clean build --no-daemon

# 5. Use JDK to run jar
FROM openjdk:21
WORKDIR /app

# 6. Copy jar from previous stage
COPY --from=build /app/build/libs/*.jar Modis.jar

# 7. Run app
ENTRYPOINT ["java", "-jar", "Modis.jar"]
