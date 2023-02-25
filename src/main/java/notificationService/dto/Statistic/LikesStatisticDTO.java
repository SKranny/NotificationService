package notificationService.dto.Statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikesStatisticDTO {
    private Long generalLikesCount;

    private Set<String> period;

    private HashMap<String, Long> likesCountByPeriod;
}
