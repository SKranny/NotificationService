package notificationService.services;

import constants.NotificationType;
import dto.friendDto.FriendsNotificationRequest;
import dto.notification.ContentDTO;
import dto.postDto.PostNotificationRequest;
import kafka.dto.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notificationService.dto.CreateNotificationRequest;
import notificationService.dto.RequestForLogger;
import notificationService.entities.Notification;
import notificationService.entities.NotificationProfile;
import notificationService.entities.Settings;
import notificationService.exception.NotificationException;
import notificationService.mapper.ContentMapper;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationProfileService notificationProfileService;

    private final ContentMapper contentMapper;

    @KafkaListener(topics = "Notification")
    public void listenerNewNotifications(CreateNotificationRequest request) {
        kafkaLogger(RequestForLogger.builder()
                .requestName("Notification")
                .content(request.getContent())
                .build());
        createNotification(request);
    }

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

    public void createNotification(CreateNotificationRequest req) {
        NotificationProfile notificationProfile = notificationProfileService.findNotificationProfileByRecipientId(req.getRecipientId());

        if (!getSettingByNotificationType(notificationProfile.getSettings(), req.getType())) {
            throw new NotificationException(String.format("Error! %s not allowed!", req.getType().name()), HttpStatus.BAD_REQUEST);
        }

        notificationProfileService.addNewNotification(req.getRecipientId(), buildNotification(req, notificationProfile));
    }

    public void createNotification(FriendsNotificationRequest req) {
        NotificationProfile notificationProfile = notificationProfileService.findNotificationProfileByRecipientId(req.getRecipientId());

        if (!getSettingByNotificationType(notificationProfile.getSettings(), req.getType())) {
            throw new NotificationException(String.format("Error! %s not allowed!", req.getType().name()), HttpStatus.BAD_REQUEST);
        }

        notificationProfileService.addNewNotification(req.getRecipientId(), buildNotification(req, notificationProfile));
    }

    public void createNotification(PostNotificationRequest req) {
        Set<NotificationProfile> notificationProfileList = notificationProfileService.findNotificationProfilesByRecipientIdList(req.getFriendsId());
        getSettingsByNotificationTypeFromList(notificationProfileList, req.getType());
        notificationProfileList.stream()
                .forEach(notificationProfile -> notificationProfileService
                        .addNewNotificationWithList(req.getFriendsId(),buildNotification(req,notificationProfile)));
    }

    private void getSettingsByNotificationTypeFromList(Set<NotificationProfile> profileList, NotificationType type){
        for (NotificationProfile profile : profileList){
           if (!getSettingByNotificationType(profile.getSettings(),type)){
               throw new NotificationException(String.format("Error! %s not allowed!", type.name()), HttpStatus.BAD_REQUEST);
           }
        }
    }

    private Notification buildNotification(CreateNotificationRequest request, NotificationProfile profile) {
        return Notification.builder()
                .authorId(request.getAuthorId())
                .content(contentMapper.toContent(request.getContent()))
                .profile(profile)
                .type(request.getType())
                .build();
    }

    private Notification buildNotification(FriendsNotificationRequest request, NotificationProfile profile) {
        return Notification.builder()
                .authorId(request.getAuthorId())
                .content(contentMapper.toContent(request.getContent()))
                .profile(profile)
                .type(request.getType())
                .build();
    }

    private Notification buildNotification(PostNotificationRequest request, NotificationProfile profile) {
        return Notification.builder()
                .authorId(request.getAuthorId())
                .content(contentMapper.toContent(request.getContent()))
                .profile(profile)
                .type(request.getType())
                .build();
    }
}
