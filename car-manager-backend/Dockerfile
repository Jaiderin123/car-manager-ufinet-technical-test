# =============================================================
#  Stage 1: Build
#  Uses Gradle to compile and package the application
# =============================================================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Gradle wrapper and dependency files first (layer caching)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Download dependencies before copying source (better cache reuse)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code and build the JAR (skip tests — run in CI pipeline)
COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# =============================================================
#  Stage 2: Runtime
#  Minimal image — only the JRE and the built JAR
# =============================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# Install tzdata to support timezone configuration
RUN apk add --no-cache tzdata

# Set Colombia timezone (UTC-5, no daylight saving time)
ENV TZ=America/Bogota
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Run as non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

# Copy only the built JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose application port
EXPOSE 8080

# JVM tuning for containerized environments
ENV JAVA_OPTS="\
  -XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=75.0 \
  -XX:+UseG1GC \
  -Djava.security.egd=file:/dev/./urandom"

# Start the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]