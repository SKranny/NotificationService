package notificationService.dto.Statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import notificationService.dto.Statistic.constant.ChartRangeType;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticRequest {
    @NotBlank
    ChartRangeType type;
}
