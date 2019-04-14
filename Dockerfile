FROM java:8-jdk-alpine

ENV KAFKA_SERVER localhost \
    KAFKA_TOPIC topic-test

WORKDIR /usr/app

COPY target/productor-0.0.1-SNAPSHOT.jar /usr/app/productor-0.0.1-SNAPSHOT.jar

RUN sh -c 'touch productor-0.0.1-SNAPSHOT.jar'

ENTRYPOINT ["java", "-jar", "/usr/app/productor-0.0.1-SNAPSHOT.jar"]
