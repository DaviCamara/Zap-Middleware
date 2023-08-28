FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} fake-news-bot-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/fake-news-bot-0.0.1-SNAPSHOT.jar"]