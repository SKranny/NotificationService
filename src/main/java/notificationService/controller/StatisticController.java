package notificationService.controller;

import lombok.RequiredArgsConstructor;
import notificationService.dto.Statistic.GeneralStatisticDTO;
import notificationService.services.PersonService;
import notificationService.services.StatisticService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping
    public GeneralStatisticDTO getGeneralStatistic(){
        return statisticService.getGeneralStatistic();
    }
}
