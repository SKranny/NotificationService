package notificationService.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notificationService.dto.Statistic.GeneralStatisticDTO;
import notificationService.dto.Statistic.PostStatisticDTO;
import notificationService.dto.Statistic.StatisticRequest;
import notificationService.dto.Statistic.UserStatisticDTO;
import notificationService.dto.Statistic.constant.ChartRangeType;
import org.joda.time.Weeks;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticService {
    private final PersonService personService;
    private final PostService postService;
    public GeneralStatisticDTO getGeneralStatistic(){
        return GeneralStatisticDTO.builder()
                .userStatistic(buildGeneralUserStats())
                .postStatistic(buildGeneralPostStats())
                .build();
    }

    public PostStatisticDTO getPostStatistic(StatisticRequest request){
        return getPostStatisticByType(request.getType());
    }

    private UserStatisticDTO buildGeneralUserStats(){
        return UserStatisticDTO.builder()
                .generalUserCount((long)personService.getAllPersonsDTO().size())
                .build();
    }

    private PostStatisticDTO buildGeneralPostStats(){
        return PostStatisticDTO.builder()
                .generalPostCount((long)postService.getAllPost().size())
                .build();
    }

    private PostStatisticDTO getPostStatisticByType(ChartRangeType type){
        switch (type) {
            case MONTH:
                return buildPostStatsByMonth();
            case WEEK:
                return buildPostStatsByWeek();
            case ALL_TIME:
                return buildPostStatsByAllTime();
            default:
                return null;
        }
    }

    private UserStatisticDTO getUserStatisticByType(ChartRangeType type){
        switch (type) {
            case MONTH:
                return buildUserStatsByMonth();
            case WEEK:
                return null;
            case ALL_TIME:
                return buildUserStatsByAllTime();
            default:
                return null;
        }
    }

    private UserStatisticDTO buildUserStatsByMonth(){
        UserStatisticDTO userStat = UserStatisticDTO.builder()
                .generalUserCount((long)personService.getAllPersonsDTO().size())
                .period(personService.getAllPersonsDTOByTimeBetween(LocalDate.now(), LocalDate.ofYearDay(LocalDate.now().getYear(),1))
                        .stream()
                        .map(personDTO -> personDTO.getCreatedOn().getMonth().name())
                        .collect(Collectors.toSet()))
                .usersCountByPeriod(new HashMap<>())
                .build();
        userStat.getPeriod().stream()
                .map(month -> userStat.getUsersCountByPeriod().put(month, (long)personService
                        .getAllPersonsDTOByTimeBetween(getFirstDayOfMonth(month), getLastDayOfMonth(month))
                        .size()));
        return userStat;
    }

    private UserStatisticDTO buildUserStatsByAllTime(){
        UserStatisticDTO userStat = UserStatisticDTO.builder()
                .generalUserCount(((long)personService.getAllPersonsDTO().size()))
                .period(personService.getAllPersonsDTO()
                        .stream()
                        .map(personDTO -> String.valueOf(personDTO.getCreatedOn().getYear()))
                        .collect(Collectors.toSet()))
                .usersCountByPeriod(new HashMap<>())
                .build();
        userStat.getPeriod().stream()
                .map(year -> userStat.getUsersCountByPeriod().put(year, (long)personService
                        .getAllPersonsDTOByTimeBetween(getFirstDayOfYear(year),getLastDayOfYear(year))
                        .size()));
        return userStat;
    }

    private PostStatisticDTO buildPostStatsByMonth(){
        PostStatisticDTO postStat = PostStatisticDTO.builder()
                .generalPostCount((long)postService.getAllPost().size())
                .period(postService.getAllPostByTimeBetween(LocalDate.now(), LocalDate.ofYearDay(LocalDate.now().getYear(),1))
                        .stream()
                        .map(postDTO -> postDTO.getTime()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate().getMonth().name())
                        .collect(Collectors.toSet()))
                .postsCountByPeriod(new HashMap<>())
                .build();
        postStat.getPeriod().stream()
                .map(month -> postStat.getPostsCountByPeriod().put(month, (long)postService
                        .getAllPostByTimeBetween(getFirstDayOfMonth(month), getLastDayOfMonth(month))
                        .size()));
        return postStat;
    }

    private PostStatisticDTO buildPostStatsByAllTime(){
        PostStatisticDTO postStat = PostStatisticDTO.builder()
                .generalPostCount((long)postService.getAllPost().size())
                .period(postService.getAllPost()
                        .stream()
                        .map(postDTO -> String.valueOf(postDTO.getTime()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate().getYear()))
                        .collect(Collectors.toSet()))
                .postsCountByPeriod(new HashMap<>())
                .build();
        postStat.getPeriod().stream()
                .map(year -> postStat.getPostsCountByPeriod().put(year, (long)postService
                        .getAllPostByTimeBetween(getFirstDayOfYear(year),getLastDayOfYear(year))
                        .size()));
        return postStat;
    }

    private PostStatisticDTO buildPostStatsByWeek(){
        PostStatisticDTO postStat = PostStatisticDTO.builder()
                .generalPostCount((long)postService.getAllPost().size())
                .period(postService
                        .getAllPostByTimeBetween(
                                getFirstDayOfMonth(LocalDate.now().getMonth().name()), LocalDate.now())
                        .stream()
                        .map(postDTO -> String.valueOf(postDTO.getTime()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate().get(WeekFields.of(Locale.getDefault()).weekOfYear())))
                        .collect(Collectors.toSet()))
                .postsCountByPeriod(new HashMap<>())
                .build();
        postStat.getPeriod().stream()
                .map(weekNumber -> postStat.getPostsCountByPeriod().put(weekNumber + " week", (long)postService
                        .getAllPostByTimeBetween(getFirstDayOfWeek(weekNumber),getLastDayOfWeek(weekNumber))
                        .size()));
        return postStat;
    }

    private LocalDate getFirstDayOfWeek(String weekNumber){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR,Integer.valueOf(weekNumber));
        calendar.set(Calendar.YEAR, LocalDate.now().getYear());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date date = calendar.getTime();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalDate getLastDayOfWeek(String weekNumber){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR,Integer.valueOf(weekNumber));
        calendar.set(Calendar.YEAR, LocalDate.now().getYear());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date date = calendar.getTime();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


    private LocalDate getFirstDayOfYear(String year){
        return LocalDate.of(Integer.parseInt(year),Month.JANUARY,1);
    }

    private LocalDate getLastDayOfYear(String year){
        return LocalDate.of(Integer.parseInt(year),Month.DECEMBER, 31);
    }

    private LocalDate getLastDayOfMonth(String month){
        return YearMonth.of(LocalDate.now().getYear(), Month.valueOf(month)).atEndOfMonth();
    }

    private LocalDate getFirstDayOfMonth(String month){
        return LocalDate.of(LocalDate.now().getYear(), Month.valueOf(month),1 );
    }
}
