package notificationService.services;

import dto.notification.NotificationDTO;
import dto.notification.SettingsDTO;
import dto.userDto.PersonDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notificationService.dto.SettingsFilter;
import notificationService.dto.UpdateSettingsRequest;
import notificationService.entities.Notification;
import notificationService.entities.NotificationProfile;
import notificationService.entities.Settings;
import notificationService.exception.NotificationException;
import notificationService.mapper.NotificationMapper;
import notificationService.mapper.SettingsMapper;
import notificationService.repository.NotificationProfileRepository;
import notificationService.repository.settings.SettingsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationProfileService {
    private final NotificationProfileRepository notificationProfileRepository;

    private final SettingsRepository settingsRepository;

    private final SettingsMapper settingsMapper;

    private final NotificationMapper notificationMapper;

    private final PersonService personService;

    public SettingsDTO getSettingsByPersonEmail(String email) {
        PersonDTO person = personService.getPersonDTOByEmail(email);
        return settingsMapper.toSettingsDTO(getOrCreateNotificationProfile(person.getId()).getSettings());
    }

    private NotificationProfile getOrCreateNotificationProfile(Long recipientId) {
        return notificationProfileRepository.findByUserId(recipientId)
                .orElseGet(() ->
                        notificationProfileRepository.save(NotificationProfile.builder()
                                .userId(recipientId)
                        .build()));
    }

    public SettingsDTO updateSettings(UpdateSettingsRequest request, String email) {
        PersonDTO person = personService.getPersonDTOByEmail(email);
        NotificationProfile profile = findNotificationProfileByRecipientId(person.getId());
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

    public List<NotificationDTO> getAllNotificationsByEmail(String email) {
        PersonDTO personDTO = personService.getPersonDTOByEmail(email);
        return notificationProfileRepository.findByUserId(personDTO.getId())
                .map(NotificationProfile::getNotifications)
                .map(notificationMapper::toListNotificationDTO)
                .orElseThrow(() -> new NotificationException("Error! Unauthorized!", HttpStatus.UNAUTHORIZED));
    }

    public Integer getNotificationsCount(String email) {
        PersonDTO personDTO = personService.getPersonDTOByEmail(email);
        return notificationProfileRepository.countNotificationsByUserId(personDTO.getId());
    }

    public NotificationProfile findNotificationProfileByRecipientId(Long recipientId) {
        return notificationProfileRepository.findByUserId(recipientId)
                .orElseThrow(() -> new NotificationException("Error! Recipient not found!", HttpStatus.BAD_REQUEST));
    }

    public List<NotificationProfile> findNotificationProfilesByRecipientIdList(List<Long> recipientIdList) {
        return notificationProfileRepository.findByUserIdIn(recipientIdList)
                .orElseThrow(() -> new NotificationException("Error! Recipient not found!", HttpStatus.BAD_REQUEST));
    }


    public void addNewNotification(Long userId, Notification notification) {
        NotificationProfile profile = findNotificationProfileByRecipientId(userId);
        profile.getNotifications().add(notification);
        notificationProfileRepository.save(profile);
    }

    public void addNewNotificationWithList(List<Long> userIdList, Notification notification){
        List<NotificationProfile> notificationProfileList = userIdList.stream()
                .map(userId -> findNotificationProfileByRecipientId(userId)).collect(Collectors.toList());
        notificationProfileList.stream()
                .map(notificationProfile -> notificationProfile.getNotifications().add(notification));
        notificationProfileRepository.saveAll(notificationProfileList);
    }
}
