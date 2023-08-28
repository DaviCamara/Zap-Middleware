FROM openjdk:17-jdk-slim
COPY --from=build /target app.jar
EXPOSE 8080
RUN mvn clean package -Dmaven.test.skip=truegit
#ENTRYPOINT ["java", "-jar", "/app.jar"]