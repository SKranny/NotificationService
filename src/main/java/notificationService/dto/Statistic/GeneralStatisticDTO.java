package notificationService.dto.Statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralStatisticDTO {
    private Long userCount;
    private Long postCount;
    private Long commentsCount;
    private Long likesCount;

}
