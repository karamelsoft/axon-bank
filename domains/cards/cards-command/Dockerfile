FROM openjdk:17-ea-32-jdk-slim

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/app/app.jar

ENTRYPOINT java ${JAVA_OPTS} -jar /usr/share/app/app.jar
