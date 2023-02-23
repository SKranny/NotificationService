package notificationService.services.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.friendDto.FriendsNotificationRequest;
import dto.postDto.PostNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import notificationService.dto.person.PersonOnline;
import notificationService.entities.Notification;
import notificationService.websocket.WebSocketSessionPool;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationReceiver {
    private final WebSocketSessionPool webSocketSessionPool;

    private final ObjectMapper jsonMapper;

    @KafkaListener(topics = "PERSON_ONLINE")
    public void handlePersonOnlineMessage(PersonOnline personOnline) {
        Set<WebSocketSession> allSessions = webSocketSessionPool.getAllSessions();
        allSessions.forEach(s -> sendMessageToSession(s, personOnline));
    }

    @KafkaListener(topics = "FRIEND_REQUEST")
    public void handleFriendRequest(FriendsNotificationRequest request) {
        WebSocketSession session = webSocketSessionPool.getCustomerSession(request.getRecipientId());
        if (session != null) {
            sendMessageToSession(session, request);
        }
    }

    @KafkaListener(topics = "POST_REQUEST")
    public void handleFriendRequest(PostNotificationRequest request) {
        Set<WebSocketSession> session = webSocketSessionPool.getPersonWebSocketSession(request.getFriendsId());
        session.forEach(s -> sendMessageToSession(s, request));
    }

    @SneakyThrows
    private void sendMessageToSession(WebSocketSession session, Object message) {
        session.sendMessage(new TextMessage(jsonMapper.writeValueAsString(message)));
    }
}
