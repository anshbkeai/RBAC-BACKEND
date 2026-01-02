# ---------- BUILD STAGE ----------
FROM eclipse-temurin:21-jdk-slim AS build
WORKDIR /app

# Copy only what's needed to cache dependencies
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Now copy source
COPY src src
RUN ./mvnw clean package -DskipTests

# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:21-jre-slim
WORKDIR /app

COPY --from=build /app/target/rdbac-0.0.1-SNAPSHOT.war app.war

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]
