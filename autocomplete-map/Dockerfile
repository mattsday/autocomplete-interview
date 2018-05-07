FROM openjdk:8-jdk-slim
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
COPY account.json /account.json
ENV GOOGLE_APPLICATION_CREDENTIALS /account.json
ENTRYPOINT ["java","-DGOOGLE_APPLICATION_CREDENTIALS=/account.json", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
