FROM maven:3.5-jdk-8 as BUILD

COPY src /usr/src/tasker/src
COPY pom.xml /usr/src/tasker
RUN mvn -f /usr/src/tasker/pom.xml clean package -DskipTests=true

FROM java:8-jre

LABEL maintainer "opsxcq@strm.sh"

COPY --from=BUILD /usr/src/tasker/target/tasker-0.0.1-SNAPSHOT.jar /tasker.jar

EXPOSE 8080

VOLUME /data

COPY main.sh /

ENTRYPOINT ["/bin/bash"]

CMD ["-c","/main.sh"]
