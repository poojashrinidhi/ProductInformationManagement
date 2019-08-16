#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /tmp/src
COPY pom.xml /tmp
RUN mvn -f /tmp/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /tmp/target/*-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 9002
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]
