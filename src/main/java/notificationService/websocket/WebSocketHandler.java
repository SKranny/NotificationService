package notificationService.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import security.dto.TokenData;
import security.utils.JwtService;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final JwtService jwtService;

    private final WebSocketSessionPool webSocketSessionPool;

    private final MessageBrokerHandler messageBrokerHandler;

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        TokenData tokenData = jwtService.parseToken((String) session.getAttributes().get("Authorization"));
        webSocketSessionPool.removeCustomerSessionFromPool(tokenData.getId());
        messageBrokerHandler.sendPersonOnlineNotification("PERSON_ONLINE", tokenData, true);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        TokenData tokenData = jwtService.parseToken((String) session.getAttributes().get("Authorization"));
        webSocketSessionPool.addCustomerSessionToPool(tokenData.getId(), session);
        messageBrokerHandler.sendPersonOnlineNotification("PERSON_ONLINE", tokenData, true);
    }

}
