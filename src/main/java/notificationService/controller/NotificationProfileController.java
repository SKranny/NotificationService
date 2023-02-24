package notificationService.controller;

import dto.notification.NotificationDTO;
import dto.notification.SettingsDTO;
import lombok.RequiredArgsConstructor;
import notificationService.dto.notification.UpdateSettingsRequest;
import notificationService.services.notification.NotificationProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import security.TokenAuthentication;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationProfileController {
    private final NotificationProfileService notificationProfileService;

    @GetMapping("/settings")
    public SettingsDTO getSettings(TokenAuthentication authentication) {
        return notificationProfileService.getSettingsByPersonId(authentication.getTokenData().getId());
    }

    @PutMapping("/settings")
    public SettingsDTO updateSettings(@Valid @RequestBody UpdateSettingsRequest request, TokenAuthentication authentication) {
        return notificationProfileService.updateSettings(request, authentication.getTokenData().getId());
    }

    @GetMapping
    public Page<NotificationDTO> getAllNotifications(
            @Valid @Min(0) @RequestParam(required = false, defaultValue = "0") Integer page,
            @Valid @Min(0) @RequestParam(required = false, defaultValue = "20") Integer offset,
            TokenAuthentication authentication) {
        return notificationProfileService.getAllNotificationsByEmail(authentication.getTokenData().getId(), PageRequest.of(page, offset));
    }

    @GetMapping("/count")
    public Long getNotificationsCount(Long personId) {
        return notificationProfileService.getNotificationsCount(personId);
    }
}
