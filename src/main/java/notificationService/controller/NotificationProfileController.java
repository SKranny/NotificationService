package notificationService.controller;

import dto.notification.NotificationDTO;
import dto.notification.SettingsDTO;
import lombok.RequiredArgsConstructor;
import notificationService.dto.UpdateSettingsRequest;
import notificationService.services.NotificationProfileService;
import org.springframework.web.bind.annotation.*;
import security.TokenAuthentication;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationProfileController {

    private final NotificationProfileService notificationProfileService;

    @GetMapping("/settings")
    public SettingsDTO getSettings(TokenAuthentication authentication) {
        return notificationProfileService.getSettingsByPersonEmail(authentication.getTokenData().getEmail());
    }

    @PutMapping("/settings")
    public SettingsDTO updateSettings(@Valid @RequestBody UpdateSettingsRequest request, TokenAuthentication authentication) {
        return notificationProfileService.updateSettings(request, authentication.getTokenData().getEmail());
    }

    @GetMapping
    public List<NotificationDTO> getAllNotifications(TokenAuthentication authentication) {
        return notificationProfileService.getAllNotificationsByEmail(authentication.getTokenData().getEmail());
    }

    @GetMapping("/count")
    public Integer getNotificationsCount(TokenAuthentication authentication) {
        return notificationProfileService.getNotificationsCount(authentication.getTokenData().getEmail());
    }

}
