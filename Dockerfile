FROM openjdk:8-jdk
MAINTAINER Khaled Ayoubi khaled.ayoubi@gmail.com

RUN mkdir -p /opt/arlo

ADD target/arlo-0.1.jar /opt/arlo/

EXPOSE 8787

CMD ["java", "-jar", "-Dspring.profiles.active=docker", "/opt/arlo/arlo-0.1.jar"]

