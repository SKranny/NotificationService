# Java Spring project "Social networking site"
### Notification-service

## Description
Notification-service is responsible for notifications to our social network. This service includes features such as:
- notifications of additions to friends
- notifications of new messages from friends
- notifications about new and unread messages
Also, within this service, a service has been implemented to collect general statistics on all events and public information in the social network.
## Service technologies
- Java version 11
- Spring Framework
- Flyway
- Lombok
- Mapstruct
- Spring Data JPA
- PostgreSQL
- Spring Security
- Spring Cloud OpenFeign
- Spring Cloud Netflix Eureka
- JWT(JsonWebToken)
- Nexus repository
- Kafka
- Swagger OpenApi
- JUnit
## Technical Description
### How to run the application on your device:
1. (Pre-configuring the PostgreSQL database) Specify in the application.yaml file, or in the environment variables in your IDE, the required application configuration parameters to run:
    - SERVER_PORT (The port of your application. Specify it manually if you are not going to use the default port: 8087)
    - NOTIFICATION_SERVICE_DATABASE_URL (The address of the database your application connects to. You should specify it manually if you are not going to use default postgresql url: jdbc:postgresql://localhost:5432/notification_service)
    - NOTIFICATION_SERVICE_DATABASE_USER (Username for the database. Specify it manually if you do not intend to use the default username: postgres)
    - NOTIFICATION_SERVICE_DATABASE_PASSWORD (Password for the database)
    - KAFKA_HOST(The address of the Kafka broker. The default host is localhost:9092. Replace it if you are not going to use the default)
    - SECRET_KEY (Your application's secret key. This is needed to protect your service which uses JWT technology)
    - EUREKA_URI (Address of your Eureka server. Specify it if you are not going to use the default address: http://localhost:8081/eureka)
2. Run the file NotificationApplication.java.
