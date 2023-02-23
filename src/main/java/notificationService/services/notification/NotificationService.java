package notificationService.services.notification;

import constants.NotificationType;
import dto.friendDto.FriendsNotificationRequest;
import dto.notification.ContentDTO;
import dto.notification.NotificationRequest;
import dto.postDto.PostNotificationRequest;
import kafka.annotation.SubmitToKafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notificationService.dto.notification.RequestForLogger;
import notificationService.entities.Notification;
import notificationService.entities.NotificationProfile;
import notificationService.entities.Settings;
import notificationService.exception.NotificationException;
import notificationService.mapper.ContentMapper;
import notificationService.repository.NotificationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationProfileService notificationProfileService;

    private final NotificationRepository notificationRepository;

    private final ContentMapper contentMapper;

    @KafkaListener(topics = "Friends")
    public void listenerNewNotifications(FriendsNotificationRequest request) {
        kafkaLogger(RequestForLogger.builder()
                .requestName("Friends")
                .content(request.getContent())
                .build());
        createNotification(request);
    }

    private void kafkaLogger(RequestForLogger request){
        log.info(String.format("[%s] processed new notification with %d attach file - %s",
                LocalDateTime.now(),
                Optional.ofNullable(request.getContent())
                        .map(ContentDTO::getAttaches)
                        .map(List::size).orElse(0),
                request)
        );
    }

    @KafkaListener(topics = "Post")
    public void listenerNewNotifications(PostNotificationRequest request) {
        kafkaLogger(RequestForLogger.builder()
                .requestName("Post")
                .content(request.getContent())
                .build());
        createNotification(request);
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

    public void createNotification(FriendsNotificationRequest req) {
        NotificationProfile notificationProfile = notificationProfileService.findNotificationProfileByRecipientId(req.getRecipientId());

        if (!getSettingByNotificationType(notificationProfile.getSettings(), req.getType())) {
            throw new NotificationException(String.format("Error! %s not allowed!", req.getType().name()), HttpStatus.BAD_REQUEST);
        }

        notificationRepository.save(buildNotification(req, notificationProfile));
        sendRequest(req);
    }
    @SubmitToKafka(topic = "FRIEND_REQUEST")
    public FriendsNotificationRequest sendRequest(FriendsNotificationRequest request) {
        return request;
    }

    @SubmitToKafka(topic = "POST_REQUEST")
    public PostNotificationRequest sendRequest(PostNotificationRequest request) {
        return request;
    }

    public void createNotification(PostNotificationRequest req) {
        Set<NotificationProfile> notificationProfileList = notificationProfileService
                .findNotificationProfilesByRecipientIdList(req.getFriendsId());
        List<NotificationProfile> profiles = getSettingsByNotificationTypeFromList(notificationProfileList, req.getType());
        List<Notification> notifications = profiles.stream()
                .map(profile -> buildNotification(req, profile))
                .collect(Collectors.toList());
        notificationRepository.saveAll(notifications);
        sendRequest(req);
    }

    private List<NotificationProfile> getSettingsByNotificationTypeFromList(Set<NotificationProfile> profileList, NotificationType type){
        return profileList.stream().filter(profile -> getSettingByNotificationType(profile.getSettings(),type)).collect(Collectors.toList());
    }

    private Notification buildNotification(NotificationRequest request, NotificationProfile profile) {
        return Notification.builder()
                .authorId(request.getAuthorId())
                .content(contentMapper.toContent(request.getContent()))
                .profile(profile)
                .type(request.getType())
                .build();
    }
}
