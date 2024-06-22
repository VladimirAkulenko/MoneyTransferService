FROM openjdk:17-jdk-slim-buster
EXPOSE 5500
ADD target/Application-0.0.1-SNAPSHOT.jar myapp.jar
ENTRYPOINT ["java", "-jar", "/myapp.jar"]