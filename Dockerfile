FROM openjdk:8u111-jdk-alpine

LABEL maintainer "opsxcq@strm.sh"

RUN useradd --system --uid 666 -M --shell /usr/sbin/nologin tasker
USER tasker
ADD /target/tasker-0.0.1-SNAPSHOT.jar /tasker.jar

EXPOSE 8080

VOLUME /data
WORKDIR /data

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/tasker.jar"]
