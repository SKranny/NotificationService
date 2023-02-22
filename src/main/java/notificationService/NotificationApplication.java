package notificationService;

import feignClient.EnableFeignClient;
import kafka.annotation.EnableKafkaClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import security.EnableMicroserviceSecurity;

@EnableKafkaClient
@EnableFeignClient
@EnableEurekaClient
@EnableMicroserviceSecurity
@SpringBootApplication
public class NotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

}
