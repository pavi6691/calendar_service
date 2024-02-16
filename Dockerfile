#
# Stage 1: Build and package project
#
FROM maven:3.9.6-eclipse-temurin-21-jammy as builder

RUN mkdir -p /app/source
COPY . /app/source
WORKDIR /app/source
#RUN ./mvnw clean package -DskipTests -Dmaven.javadoc.skip=true
RUN mvn clean package -DskipTests -Dmaven.javadoc.skip=true


#
# Stage 2: Create the final image
#
FROM eclipse-temurin:21.0.2_13-jre-jammy

VOLUME /tmp

# Copy the packaged JAR file from the build stage
COPY --from=builder /app/source/services/rest/target/*.jar /app/app.jar

ARG JAR_FILE=target/*.jar
# Define environment variable
ENV JAVA_OPTS=""


# Expose the port the app runs on
EXPOSE 8001

# Run the application when the container starts
ENTRYPOINT ["java", "-jar", "/app/app.jar"]