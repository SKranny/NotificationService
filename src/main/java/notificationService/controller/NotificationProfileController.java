package notificationService.controller;

import dto.notification.NotificationDTO;
import dto.notification.SettingsDTO;
import lombok.RequiredArgsConstructor;
import notificationService.dto.CreateSettingsRequest;
import notificationService.dto.UpdateSettingsRequest;
import notificationService.services.NotificationProfileService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationProfileController {

    private final NotificationProfileService notificationProfileService;

    @GetMapping("/settings")
    public SettingsDTO getSettings(Principal principal) {
        return notificationProfileService.getSettingsByPersonEmail(principal.getName());
    }

    @PutMapping("/settings")
    public SettingsDTO updateSettings(@Valid @RequestBody UpdateSettingsRequest request, Principal principal) {
        return notificationProfileService.updateSettings(request, principal.getName());
    }

    @PostMapping("/settings")
    public SettingsDTO createSettings(@Valid @RequestBody CreateSettingsRequest request, Principal principal) {
        return notificationProfileService.createSettings(request, principal.getName());
    }

    @GetMapping
    public List<NotificationDTO> getAllNotifications(Principal principal) {
        return notificationProfileService.getAllNotificationsByEmail(principal.getName());
    }

    @GetMapping("/count")
    public Integer getNotificationsCount(Principal principal) {
        return notificationProfileService.getNotificationsCount(principal.getName());
    }

}
