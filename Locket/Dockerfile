# Use a lightweight JDK 21 image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy the jar file built by Gradle (adjust the name if needed)
COPY build/libs/*.jar app.jar

# Expose port Spring Boot runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "Modis.jar"]
