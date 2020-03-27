FROM openjdk:13-alpine

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/app/app.jar

ENTRYPOINT java ${JAVA_OPTS} -jar /usr/share/app/app.jar
