package notificationService.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import notificationService.dto.Statistic.*;
import notificationService.dto.Statistic.constant.ChartRangeType;
import notificationService.exception.NotificationException;
import notificationService.services.StatisticService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications/statistic")
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
    @ResponseBody
    public PostStatisticDTO getPostStatistic(@RequestBody StatisticRequest request){
        return statisticService.getPostStatistic(request);
    }

    @Operation(summary = "Получение статистики для публикаций")
    @GetMapping("/users")
    @ResponseBody
    public UserStatisticDTO getUserStatistic(@RequestBody StatisticRequest request){
        return statisticService.getUserStatistic(request);
    }

    @Operation(summary = "Получение статистики для комментариев")
    @GetMapping("/comments")
    @ResponseBody
    public CommentsStatisticDTO getCommentStatistic(@RequestBody StatisticRequest request){
        return statisticService.getCommentsStatistic(request);
    }
    @Operation(summary = "Получение статистики для лайков")
    @GetMapping("/likes")
    @ResponseBody
    public LikesStatisticDTO getLikeStatistic(@RequestBody StatisticRequest request){
        return statisticService.getLikesStatistic(request);
    }

}
