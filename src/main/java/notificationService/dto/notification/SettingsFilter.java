package notificationService.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettingsFilter {
    private boolean friendRequest;

    private boolean friendBirthday;

    private boolean postComment;

    private boolean commentComment;

    private boolean post;

    private boolean message;

    private boolean sendPhoneMessage;

    private boolean sendEmailMessage;
}
