package notificationService.dto.notification;

import dto.notification.ContentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestForLogger{
    private String requestName;
    private ContentDTO content;
}
