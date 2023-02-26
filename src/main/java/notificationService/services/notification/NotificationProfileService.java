package notificationService.services.notification;

import dto.notification.NotificationDTO;
import dto.notification.SettingsDTO;
import dto.userDto.PersonDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notificationService.dto.notification.SettingsFilter;
import notificationService.dto.notification.UpdateSettingsRequest;
import notificationService.entities.Notification;
import notificationService.entities.NotificationProfile;
import notificationService.entities.Settings;
import notificationService.exception.NotificationException;
import notificationService.mapper.NotificationMapper;
import notificationService.mapper.SettingsMapper;
import notificationService.repository.NotificationProfileRepository;
import notificationService.repository.NotificationRepository;
import notificationService.repository.settings.SettingsRepository;
import notificationService.services.PersonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationProfileService {
    private final NotificationRepository notificationRepository;
    private final NotificationProfileRepository notificationProfileRepository;

    private final SettingsRepository settingsRepository;

    private final SettingsMapper settingsMapper;

    private final NotificationMapper notificationMapper;

    private final PersonService personService;

    public SettingsDTO getSettingsByPersonId(Long personId) {
        return settingsMapper.toSettingsDTO(getOrCreateNotificationProfile(personId).getSettings());
    }

    private NotificationProfile getOrCreateNotificationProfile(Long recipientId) {
        return notificationProfileRepository.findByUserId(recipientId)
                .orElseGet(() ->
                        notificationProfileRepository.save(NotificationProfile.builder()
                                .userId(recipientId)
                        .build()));
    }

    public SettingsDTO updateSettings(UpdateSettingsRequest request, Long personId) {
        NotificationProfile profile = findNotificationProfileByRecipientId(personId);
        profile.setSettings(getOrBuildSettings(buildSettingsFilter(profile.getSettings(), request)));
        return settingsMapper.toSettingsDTO(notificationProfileRepository.save(profile).getSettings());
    }

    private SettingsFilter buildSettingsFilter(Settings settings, UpdateSettingsRequest request) {
        SettingsFilter filter = SettingsFilter.builder()
                .commentComment(settings.getCommentOnComment())
                .postComment(settings.getPostComment())
                .sendPhoneMessage(settings.getPhoneNotification())
                .message(settings.getMessage())
                .sendEmailMessage(settings.getEmailNotification())
                .friendBirthday(settings.getFriendBirthday())
                .friendRequest(settings.getFriendRequest())
                .post(settings.getPost())
                .build();
        updateSettingsField(filter, request);
        return filter;
    }

    private void updateSettingsField(SettingsFilter settings, UpdateSettingsRequest request) {
        switch (request.getNotificationType()) {
            case MESSAGE:
                settings.setMessage(request.getEnable());
                break;
            case POST_COMMENT:
                settings.setPostComment(request.getEnable());
                break;
            case FRIEND_REQUEST:
                settings.setFriendRequest(request.getEnable());
                break;
            case COMMENT_COMMENT:
                settings.setCommentComment(request.getEnable());
                break;
            case POST:
                settings.setPost(request.getEnable());
                break;
            case SEND_EMAIL_MESSAGE:
                settings.setSendEmailMessage(request.getEnable());
                break;
            case SEND_PHONE_MESSAGE:
                settings.setSendPhoneMessage(request.getEnable());
                break;
            case FRIEND_BIRTHDAY:
                settings.setFriendBirthday(request.getEnable());
                break;
            default:
                throw new NotificationException("Error! Unknown notification type!");
        }
    }

    @KafkaListener(topics = "NewCustomer")
    public SettingsDTO createSettings(PersonDTO personDTOEvent) {
        NotificationProfile notificationProfile = findOrBuildNotificationProfile(personDTOEvent.getId());
        notificationProfile.setSettings(getOrBuildSettings(new SettingsFilter()));
        notificationProfileRepository.save(notificationProfile);
        return settingsMapper.toSettingsDTO(notificationProfile.getSettings());
    }

    private Settings getOrBuildSettings(SettingsFilter request) {
        return settingsRepository.findSettingsProfile(request)
                .orElseGet(() -> buildSettingsFromFilter(request));
    }

    private NotificationProfile findOrBuildNotificationProfile(Long userId) {
        return notificationProfileRepository.findByUserId(userId)
                .orElseGet(() -> NotificationProfile.builder()
                        .userId(userId)
                        .build());
    }

    private Settings buildSettingsFromFilter(SettingsFilter filter) {
        return Settings.builder()
                .phoneNotification(filter.isSendPhoneMessage())
                .commentOnComment(filter.isCommentComment())
                .emailNotification(filter.isSendEmailMessage())
                .friendBirthday(filter.isFriendBirthday())
                .friendRequest(filter.isFriendRequest())
                .postComment(filter.isPostComment())
                .message(filter.isMessage())
                .post(filter.isPost())
                .build();
    }

    public Page<NotificationDTO> getAllNotificationsByEmail(Long personId, PageRequest pageRequest) {
        List<Notification> notifications = notificationRepository.findAllByProfile_UserIdOrderByIsSent(personId, pageRequest)
                .getContent().stream().peek(n -> n.setIsSent(true)).collect(Collectors.toList());;
        return new PageImpl<>(
                notifications.stream().map(notificationMapper::toNotificationDTO)
                    .collect(Collectors.toList()),
                pageRequest,
                notificationRepository.countAllByProfile_UserId(personId));
    }

    public NotificationProfile findNotificationProfileByRecipientId(Long recipientId) {
        return notificationProfileRepository.findByUserId(recipientId)
                .orElseThrow(() -> new NotificationException("Error! Recipient not found!", HttpStatus.BAD_REQUEST));
    }

    public Set<NotificationProfile> findNotificationProfilesByRecipientIdList(List<Long> recipientIdList) {
        Map<Long, NotificationProfile> profiles = notificationProfileRepository.findByUserIdIn(recipientIdList).stream()
                .collect(Collectors.toMap(NotificationProfile::getId, profile -> profile));
        Set<Long> lostNotificationProfiles = recipientIdList.stream().filter(id -> !profiles.containsKey(id))
                .collect(Collectors.toSet());
        log.info(String.format("Customers with ids [%s] not found", lostNotificationProfiles));
        return new HashSet<>(profiles.values());
    }

    public Long getNotificationsCount(Long personId) {
        return notificationRepository.countAllByProfile_UserId(personId);
    }
}
