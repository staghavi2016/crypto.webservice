FROM openjdk:18-jdk

COPY config.yml /data/crypto.webservice/config.yml
COPY /target/crypto.webservice-1.0-SNAPSHOT.jar /data/crypto.webservice/crypto.webservice-1.0-SNAPSHOT.jar

WORKDIR /data/crypto.webservice

RUN java -version

CMD ["java","-jar","crypto.webservice-1.0-SNAPSHOT.jar","server","config.yml"]

EXPOSE 8080-8081
