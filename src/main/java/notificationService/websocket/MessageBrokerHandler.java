package notificationService.websocket;

import lombok.RequiredArgsConstructor;
import notificationService.dto.person.PersonOnline;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import security.dto.TokenData;

@Component
@RequiredArgsConstructor
public class MessageBrokerHandler {

    private final KafkaTemplate<Long, Object> kafkaTemplate;

    public void sendPersonOnlineNotification(String topic, TokenData data, Boolean isOnline) {
        PersonOnline personOnline = PersonOnline.builder()
                .personId(data.getId())
                .userName(data.getUserName())
                .isOnline(isOnline)
                .build();
        kafkaTemplate.send(topic, personOnline);
    }

}
