package notificationService.services;

import dto.notification.NotificationDTO;
import dto.notification.SettingsDTO;
import dto.userDto.PersonDTO;
import lombok.RequiredArgsConstructor;
import notificationService.dto.CreateSettingsRequest;
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
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
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

    public SettingsDTO createSettings(CreateSettingsRequest request, String email) {
        PersonDTO personDTO = personService.getPersonDTOByEmail(email);
        NotificationProfile notificationProfile = findOrBuildNotificationProfile(personDTO.getId());
        notificationProfile.setSettings(getOrBuildSettings(buildSettingsFilterFromRequest(request)));
        return settingsMapper.toSettingsDTO(notificationProfileRepository.save(notificationProfile).getSettings());
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

    private SettingsFilter buildSettingsFilterFromRequest(CreateSettingsRequest request) {
        return SettingsFilter.builder()
                .post(Optional.ofNullable(request.getPost()).orElse(false))
                .friendRequest(Optional.ofNullable(request.getFriendRequest()).orElse(false))
                .friendBirthday(Optional.ofNullable(request.getFriendBirthday()).orElse(false))
                .sendEmailMessage(Optional.ofNullable(request.getSendEmailMessage()).orElse(false))
                .message(Optional.ofNullable(request.getMessage()).orElse(false))
                .sendPhoneMessage(Optional.ofNullable(request.getSendPhoneMessage()).orElse(false))
                .postComment(Optional.ofNullable(request.getPostComment()).orElse(false))
                .commentComment(Optional.ofNullable(request.getCommentComment()).orElse(false))
                .build();
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
        NotificationProfile profile = notificationProfileRepository.findByUserId(personDTO.getId()).get();
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

    public void addNewNotification(Long userId, Notification notification) {
        NotificationProfile profile = findNotificationProfileByRecipientId(userId);
        profile.getNotifications().add(notification);
        notificationProfileRepository.save(profile);
    }
}
