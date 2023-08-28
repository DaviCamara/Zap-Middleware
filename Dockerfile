FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY --from=build /target .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]