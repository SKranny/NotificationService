package notificationService.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import notificationService.dto.Statistic.GeneralStatisticDTO;
import notificationService.dto.Statistic.PostStatisticDTO;
import notificationService.dto.Statistic.StatisticRequest;
import notificationService.dto.Statistic.UserStatisticDTO;
import notificationService.dto.Statistic.constant.ChartRangeType;
import notificationService.exception.NotificationException;
import notificationService.services.StatisticService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @Operation(summary = "Получение общей статистики")
    @GetMapping
    @ResponseBody
    public GeneralStatisticDTO getGeneralStatistic(){
        return statisticService.getGeneralStatistic();
    }

    @Operation(summary = "Получение статистики для публикаций")
    @GetMapping("/posts")
    public PostStatisticDTO getPostStatistic(@RequestBody StatisticRequest request){
        return statisticService.getPostStatistic(request);
    }

    @Operation(summary = "Получение статистики для публикаций")
    @GetMapping("/users")
    public UserStatisticDTO getUserStatistic(@RequestBody StatisticRequest request){
        return statisticService.getUserStatistic(request);
    }

}
