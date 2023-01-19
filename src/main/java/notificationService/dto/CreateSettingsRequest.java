package notificationService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateSettingsRequest {

    @JsonProperty(defaultValue = "false")
    private Boolean friendRequest;

    @JsonProperty(defaultValue = "false")
    private Boolean friendBirthday;

    @JsonProperty(defaultValue = "false")
    private Boolean postComment;

    @JsonProperty(defaultValue = "false")
    private Boolean commentComment;

    @JsonProperty(defaultValue = "false")
    private Boolean post;

    @JsonProperty(defaultValue = "false")
    private Boolean message;

    @JsonProperty(defaultValue = "false")
    private Boolean sendPhoneMessage;

    @JsonProperty(defaultValue = "false")
    private Boolean sendEmailMessage;
}
