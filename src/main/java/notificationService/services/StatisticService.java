package notificationService.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notificationService.dto.Statistic.GeneralStatisticDTO;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticService {
    private final PersonService personService;
    private final PostService postService;
    public GeneralStatisticDTO getGeneralStatistic(){
        return GeneralStatisticDTO.builder()
                .userCount(((long)personService.getAllPersonsDTO().size()))
                .postCount((long)postService.getAllPosts().size())
                .build();
    }
}
