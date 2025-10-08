# GameAuth Dockerfile
# Multi-stage build for optimized image size

# Build stage
FROM maven:3.8.6-openjdk-8-slim AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:8-jre-slim

# Install necessary packages
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

# Create app user for security
RUN groupadd -r gameauth && useradd -r -g gameauth gameauth

# Set working directory
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/gameauth-*.jar app.jar

# Change ownership to app user
RUN chown -R gameauth:gameauth /app

# Switch to non-root user
USER gameauth

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
