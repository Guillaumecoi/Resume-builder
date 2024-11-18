# Use Java 21 as the base image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven

# Expose main port
EXPOSE 8080

# Set development profile
ENV SPRING_PROFILES_ACTIVE=dev

# Run Spring Boot with development configuration
ENTRYPOINT ["mvn", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"]