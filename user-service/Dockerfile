# Stage 1: Build using the Java 21 JDK (Alpine)
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace/app

# Copy the wrapper and project files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Build the application
RUN ./mvnw install -DskipTests

# Stage 2: Runtime using the Java 21 JRE (Alpine)
FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
# Expose the service port (change this per service, e.g., 8082 for User)
EXPOSE 8082
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]