FROM openjdk:8u111-jdk-alpine

LABEL maintainer "opsxcq@strm.sh"

ADD target/tasker-0.0.1-SNAPSHOT.jar /tasker.jar

EXPOSE 8080

VOLUME /data

COPY main.sh /

ENTRYPOINT ["/main.sh]
