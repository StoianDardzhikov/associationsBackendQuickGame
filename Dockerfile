# ---- build stage: Maven + JDK exist ONLY here, never on the server ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom first so dependencies are cached in their own layer and are not
# re-downloaded every time a source file changes.
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline || true

COPY src ./src
RUN mvn -B -DskipTests package

# ---- runtime stage: small JRE, no build tools ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/associationGame-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
