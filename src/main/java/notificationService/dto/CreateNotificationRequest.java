package notificationService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import constants.NotificationType;
import dto.notification.ContentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateNotificationRequest {
    private Long authorId;

    private Long recipientId;

    @JsonProperty("notificationType")
    private NotificationType type;

    private ContentDTO content;
}
