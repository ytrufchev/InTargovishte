FROM openjdk:17-jdk-alpine
COPY target/InTargovishte-0.0.1-SNAPSHOT.jar App.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","App.jar"]