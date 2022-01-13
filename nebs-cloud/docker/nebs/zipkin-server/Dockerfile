FROM openjdk:8u212-jre
MAINTAINER number47
COPY zipkin.jar /nebs/zipkin-server/zipkin.jar
ENTRYPOINT ["java", "-Xmx256m", "-jar", "/nebs/zipkin-server/zipkin.jar"]