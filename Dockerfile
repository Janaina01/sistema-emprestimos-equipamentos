# Etapa 1: compilar o projeto com Maven e Java 21
FROM maven:3-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN mvn -q -DskipTests package

# Etapa 2: executar a aplicação com Java 21
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx350m", "-jar", "app.jar"]