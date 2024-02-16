FROM openjdk:17-slim as builder
RUN mkdir -p /app/source
COPY . /app/source
WORKDIR /app/source
RUN ./mvnw clean package -DskipTests -Dmaven.javadoc.skip=true

# Stage 2: Create the final image
FROM openjdk:17-slim

VOLUME /tmp

# Copy the packaged JAR file from the build stage
COPY --from=builder /app/source/services/rest/target/*.jar /app/app.jar
# Expose the port the app runs on
EXPOSE 8001

ARG JAR_FILE=target/*.jar
# Define environment variable
ENV JAVA_OPTS=""

# Run the application when the container starts
ENTRYPOINT ["java", "-jar", "/app/app.jar"]