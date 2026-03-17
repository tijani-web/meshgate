FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /workspace/app

# Copy the wrapper files (Make sure these exist in your billing folder!)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests

FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]