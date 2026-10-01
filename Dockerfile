# syntax=docker/dockerfile:1
# =====================================================================
# MULTI-STAGE image for transaction-limits-api (Spring Boot 4.x, Java 21).
#
#   Stage 1 (build):  Maven + JDK 21 compiles and packages the fat JAR.
#   Stage 2 (runtime): slim JRE 21 with the JAR only (no build tools).
#
# The dependency com.vhre:base-project-spring-boot-starter lives on GitHub
# Packages, so the build needs ~/.m2/settings.xml (server "github", PAT with
# read:packages). It is injected as a BuildKit SECRET: it is used during the
# build and never stored in the image or in any layer.
#
# Build with Docker:
#     docker build --secret id=maven_settings,src=$HOME/.m2/settings.xml \
#                  -t transaction-limits-api:latest .
#
# Build with Docker Compose (the secret is wired in docker-compose.yml):
#     docker compose up -d --build
# =====================================================================

# ---------- Stage 1: build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Step 1: resolve dependencies (cached layer, rebuilt only when pom changes).
COPY pom.xml .
RUN --mount=type=secret,id=maven_settings,target=/root/.m2/settings.xml \
    mvn -B dependency:go-offline

# Step 2: compile and package (tests are skipped: Testcontainers needs Docker
# and the JAR is already verified by the local build).
COPY src ./src
RUN --mount=type=secret,id=maven_settings,target=/root/.m2/settings.xml \
    mvn -B -DskipTests package

# ---------- Stage 2: runtime ----------
FROM eclipse-temurin:21-jre

LABEL org.opencontainers.image.title="transaction-limits-api" \
      org.opencontainers.image.description="Transaction limits REST API (Spring Boot 4.x)"

WORKDIR /app

# target/transaction-limits-api.jar (stable name defined with finalName in pom.xml).
COPY --from=build /workspace/target/transaction-limits-api.jar app.jar

# Default profile of the image: prod (configuration 100% via environment
# variables, Swagger disabled). The local docker-compose overrides it to
# "dev" so Swagger UI is available. Variables expected by the profiles:
#   DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD, REDIS_HOST, REDIS_PORT,
#   REDIS_PASSWORD, JWT_SECRET  (+ APP_PROFILE, JAVA_OPTS).
ENV APP_PROFILE=prod \
    JAVA_OPTS=""

# HTTP port of the service (server.port in application.yml).
EXPOSE 8080

# Dedicated non-privileged user (this base image does not ship one).
RUN useradd --system --uid 1001 --no-create-home appuser
USER appuser

# "exec java": java stays as PID 1 and receives the SIGTERM from "docker stop"
# for a clean shutdown of the HikariCP pool and the Redis connections.
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
