FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /build
ADD ./target/notificationService-0.0.1-SNAPSHOT.jar ./notification-service.jar
EXPOSE 8087
CMD java -jar notification-service.jar
