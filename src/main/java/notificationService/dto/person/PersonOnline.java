package notificationService.dto.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonOnline {
    private Long personId;

    private String userName;

    private Boolean isOnline;
}
