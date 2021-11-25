FROM amazoncorretto:11-alpine
RUN apk --no-cache add curl
VOLUME /tmp
ARG URI
ENV DB_URI=${URI}
ARG JAR_FILE=build/libs/voting-session-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]