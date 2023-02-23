package notificationService.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WebSocketSessionPool {
    private final Map<Long, WebSocketSession> webSocketSessionPool = new HashMap<>();

    public void addCustomerSessionToPool(@NonNull Long personId, @NonNull WebSocketSession session) {
        webSocketSessionPool.put(personId, session);
        log.info(String.format("The user [%s] session was registered!", personId));
    }

    public void removeCustomerSessionFromPool(@NonNull Long personId) {
        webSocketSessionPool.remove(personId);
        log.info(String.format(String.format("The user [%s] session was deleted", personId)));
    }

    public WebSocketSession getCustomerSession(@NonNull Long personId) {
        return webSocketSessionPool.get(personId);
    }

    public Set<WebSocketSession> getPersonWebSocketSession(@NonNull Collection<Long> personIds) {
        return personIds.stream().map(webSocketSessionPool::get).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public Set<WebSocketSession> getAllSessions() {
        return new HashSet<>(webSocketSessionPool.values());
    }
}
