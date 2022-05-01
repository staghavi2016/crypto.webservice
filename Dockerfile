### Build Stage ###

FROM maven:3.6.0-jdk-11-slim AS build

COPY pom.xml /tmp/pom.xml
RUN mvn -B -f /tmp/pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve

COPY src /data/crypto.webservice/src
COPY pom.xml /data/crypto.webservice
RUN mvn -f /data/crypto.webservice/pom.xml clean package

### Package Stage ###

FROM openjdk:18-jdk
COPY config.yml /data/crypto.webservice/config.yml
COPY --from=build /data/crypto.webservice/target/crypto.webservice-1.0-SNAPSHOT.jar /data/crypto.webservice

WORKDIR /data/crypto.webservice

RUN java -version

CMD ["java","-jar","crypto.webservice-1.0-SNAPSHOT.jar","server","config.yml"]

EXPOSE 8080-8081
