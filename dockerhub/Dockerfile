FROM maven:3.8.4-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:11-jre
COPY --from=build /home/app/target/docker-*SNAPSHOT.jar /opt/app.jar
EXPOSE 1603
ENTRYPOINT ["java","-jar","/opt/app.jar"]