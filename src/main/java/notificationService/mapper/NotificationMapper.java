package notificationService.mapper;

import dto.notification.NotificationDTO;
import lombok.RequiredArgsConstructor;
import notificationService.entities.Notification;
import notificationService.exception.NotificationException;
import notificationService.repository.NotificationProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationMapper {
    private final ContentMapper contentMapper;

    private final NotificationProfileRepository notificationProfileRepository;

    public Notification toNotification(NotificationDTO notificationDTO) {
        if (notificationDTO == null) {
            return null;
        }

        return Notification.builder()
                .id(notificationDTO.getId())
                .authorId(notificationDTO.getAuthorId())
                .type(notificationDTO.getType())
                .content(contentMapper.toContent(notificationDTO.getContent()))
                .createDatetime(notificationDTO.getCreateDatetime())
                .sendDatetime(notificationDTO.getSendDatetime())
                .profile(notificationProfileRepository.findByUserId(notificationDTO.getRecipientId())
                        .orElseThrow(() -> new NotificationException("Error! Invalid request", HttpStatus.BAD_REQUEST)))
                .isSent(notificationDTO.getIsSent())
                .build();
    }

    public NotificationDTO toNotificationDTO(Notification notification) {
        if (notification == null) {
            return null;
        }

        return NotificationDTO.builder()
                .id(notification.getId())
                .authorId(notification.getId())
                .recipientId(notification.getProfile().getUserId())
                .type(notification.getType())
                .content(contentMapper.toContentDTO(notification.getContent()))
                .createDatetime(notification.getCreateDatetime())
                .sendDatetime(notification.getSendDatetime())
                .isSent(notification.getIsSent())
                .build();
    }

    public List<NotificationDTO> toListNotificationDTO(List<Notification> notifications) {
        return notifications.stream()
                .map(this::toNotificationDTO)
                .collect(Collectors.toList());
    }
}
