FROM maven:3.8.3-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ /app/src/
RUN mvn package -DskipTests
EXPOSE 5000
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/CurrencyConversionService-1.0.0.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]