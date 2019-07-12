FROM wangyuheiyi/maven-docker-client:latest
ADD gametoollog-0.0.1-SNAPSHOT.jar /gametoollog.jar
RUN mkdir -p /data/myobj/output
EXPOSE 8092
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/gametoollog.jar"]