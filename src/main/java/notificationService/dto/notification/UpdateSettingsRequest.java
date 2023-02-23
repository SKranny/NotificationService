package notificationService.dto.notification;

import constants.NotificationType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateSettingsRequest {
    @NotNull
    private Boolean enable;

    @NotNull
    private NotificationType notificationType;
}
