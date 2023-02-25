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
    private UserStatisticDTO userStatistic;
    private PostStatisticDTO postStatistic;
    private CommentsStatisticDTO commentStatistic;
    private LikesStatisticDTO likeStatistic;

}
