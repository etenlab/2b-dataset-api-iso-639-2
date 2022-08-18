FROM amazoncorretto:18
EXPOSE 8302
ARG JAR_FILE=target/*.jar
COPY build/libs/iso-639-2-api-0.0.1-SNAPSHOT.jar app.jar
COPY build/resources/main/application.yml application.yml
ENTRYPOINT ["java","-jar","/app.jar"]
