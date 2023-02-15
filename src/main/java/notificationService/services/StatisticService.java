package notificationService.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notificationService.dto.Statistic.GeneralStatisticDTO;
import notificationService.dto.Statistic.PostStatisticDTO;
import notificationService.dto.Statistic.UserStatisticDTO;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticService {
    private final PersonService personService;
    private final PostService postService;
    public GeneralStatisticDTO getGeneralStatistic(){
        return GeneralStatisticDTO.builder()
                .userStatistic(buildUserStats())
                .postStatistic(buildPostStats())
                .build();
    }

    public PostStatisticDTO getPostStatistic(){
        return buildPostStats();
    }

    private UserStatisticDTO buildUserStats(){
        return UserStatisticDTO.builder()
                .userCount((long)personService.getAllPersonsDTO().size())
                .build();
    }

    private PostStatisticDTO buildPostStats(){
        return PostStatisticDTO.builder()
                .postCount((long)postService.getAllPosts().size())
                .build();
    }
}
