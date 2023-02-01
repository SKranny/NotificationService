package notificationService.services;

import constants.NotificationType;
import dto.notification.ContentDTO;
import kafka.dto.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notificationService.dto.CreateNotificationRequest;
import notificationService.entities.Notification;
import notificationService.entities.NotificationProfile;
import notificationService.entities.Settings;
import notificationService.exception.NotificationException;
import notificationService.mapper.ContentMapper;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationProfileService notificationProfileService;

    private final ContentMapper contentMapper;

    @KafkaListener(topics = "Notification")
    public void createNotification(Event<CreateNotificationRequest> event) {
        log.info(String.format("%s [%s] request new notification with %d attach file - %s",
                event.getServiceName(),
                event.getTime(),
                Optional.ofNullable(event.getContent().getContent())
                        .map(ContentDTO::getAttaches)
                        .map(List::size).orElse(0),
                event.getContent().getContent().getText())
        );
        createNotification(event.getContent());
    }

    private boolean getSettingByNotificationType(Settings settings, NotificationType type) {
        switch (type) {
            case MESSAGE:
                return settings.getMessage();
            case POST_COMMENT:
                return settings.getPostComment();
            case FRIEND_REQUEST:
                return settings.getFriendRequest();
            case COMMENT_COMMENT:
                return settings.getCommentOnComment();
            case POST:
                return settings.getPost();
            case SEND_EMAIL_MESSAGE:
                return settings.getEmailNotification();
            case FRIEND_BIRTHDAY:
                return settings.getFriendBirthday();
            case SEND_PHONE_MESSAGE:
                return settings.getPhoneNotification();
            default:
                throw new NotificationException("Error! Unknown notification type!");
        }
    }

    public void createNotification(CreateNotificationRequest req) {
        NotificationProfile notificationProfile = notificationProfileService.findNotificationProfileByRecipientId(req.getRecipientId());

        if (!getSettingByNotificationType(notificationProfile.getSettings(), req.getType())) {
            throw new NotificationException(String.format("Error! %s not allowed!", req.getType().name()), HttpStatus.BAD_REQUEST);
        }

        notificationProfileService.addNewNotification(req.getRecipientId(), buildNotification(req, notificationProfile));
    }

    private Notification buildNotification(CreateNotificationRequest request, NotificationProfile profile) {
        return Notification.builder()
                .authorId(request.getAuthorId())
                .content(contentMapper.toContent(request.getContent()))
                .profile(profile)
                .type(request.getType())
                .build();
    }
}
