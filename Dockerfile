# Etapa de construcción con Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace/app

# Copiar solo los archivos necesarios para la construcción
COPY pom.xml .
COPY src src/

# Construir la aplicación
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiar el JAR construido desde la etapa de construcción
COPY --from=build /workspace/app/target/*.jar app.jar

# Puerto en el que se ejecutará la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
