FROM amazoncorretto:17-alpine
WORKDIR /app

COPY build/libs/*.jar carered.jar

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar carered.jar"]
