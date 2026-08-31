# Builder stage
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /application

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY checkstyle.xml .

RUN chmod +x mvnw

COPY src src

RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /application

COPY --from=builder /application/target/*.jar application.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]
