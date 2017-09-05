FROM openjdk:8u111-jdk-alpine

LABEL maintainer "opsxcq@strm.sh"

ADD target/tasker-0.0.1-SNAPSHOT.jar /tasker.jar

EXPOSE 8080

VOLUME /data
VOLUME /application.yml
VOLUME /var/run/docker.sock

WORKDIR /data

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/tasker.jar"]
