FROM gradle:5.3.0-jdk-alpine AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME

COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src

RUN gradle build || return 0
COPY . .
RUN gradle clean build
RUN gradle bootJar

FROM openjdk:11
EXPOSE 8080
ARG JAR_FILE=build/libs/biometric-consumer-demo-1.0.0.jar
ARG SPRING_MVC_ASYNC_REQUESTTIMEOUT=10000
ARG SUNEDU_CONNECTTIMEOUT=40000
ARG SUNEDU_READTIMEOUT=25000
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]