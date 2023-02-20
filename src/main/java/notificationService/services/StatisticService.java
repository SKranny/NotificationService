package notificationService.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notificationService.dto.Statistic.*;
import notificationService.dto.Statistic.constant.BetweenDataRequest;
import notificationService.dto.Statistic.constant.ChartRangeType;
import notificationService.exception.StatisticException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.WeekFields;
import java.util.*;
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

    public UserStatisticDTO getUserStatistic(StatisticRequest request){
        return getUserStatisticByType(request.getType());
    }

    private UserStatisticDTO buildGeneralUserStats(){
        return UserStatisticDTO.builder()
                .generalUserCount((long)personService.getAllPersonsDTO().size())
                .build();
    }

    private PostStatisticDTO buildGeneralPostStats(){
        return PostStatisticDTO.builder()
                .generalPostCount((long)postService.getAllPosts().size())
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
                throw new StatisticException("Error! Unknown chart type!");
        }
    }

    /*private CommentsStatisticDTO getCommentsStatisticByType(ChartRangeType type){
        switch (type) {
            case MONTH:
                return buildCommentsStatsByMonth;
            case WEEK:
                return buildCommentsStatsByWeek;
            case ALL_TIME:
                return buildCommentsStatsByAllTime();
            default:
                throw new StatisticException("Error! Unknown chart type!");
        }
    }*/

    /*private CommentsStatisticDTO buildCommentsStatsByAllTime() {
        return CommentsStatisticDTO.builder()
                .generalCommentsCount()
                .period()
                .commentsCountByPeriod()
                .build();
    }*/

    private UserStatisticDTO getUserStatisticByType(ChartRangeType type){
        switch (type) {
            case MONTH:
                return buildUserStatsByMonth();
            case WEEK:
                return buildUserStatsByWeek();
            case ALL_TIME:
                return buildUserStatsByAllTime();
            default:
                throw new StatisticException("Error! Unknown chart type!");
        }
    }

    private UserStatisticDTO buildUserStatsByWeek() {
        UserStatisticDTO userStat = UserStatisticDTO.builder()
                .generalUserCount((long)personService.getAllPersonsDTO().size())
                .period(personService
                        .getAllPersonsDTOByTimeBetween(BetweenDataRequest.builder()
                                .date1(getFirstDayOfMonth(LocalDate.now().getMonth().name()))
                                .date2(LocalDate.now())
                                .build())
                        .stream()
                        .map(personDTO -> String.valueOf(personDTO.getCreatedOn()
                                .toLocalDate().get(WeekFields.of(Locale.getDefault()).weekOfYear())))
                        .collect(Collectors.toSet()))
                .usersCountByPeriod(new HashMap<>())
                .build();
        userStat.getPeriod().stream()
                .map(weekNumber -> userStat.getUsersCountByPeriod().put(weekNumber + " week", (long)personService
                        .getAllPersonsDTOByTimeBetween(BetweenDataRequest.builder()
                                .date1(getFirstDayOfWeek(weekNumber))
                                .date2(getLastDayOfWeek(weekNumber))
                                .build())
                        .size()));
        return userStat;
    }

    private UserStatisticDTO buildUserStatsByMonth(){
        UserStatisticDTO userStat = UserStatisticDTO.builder()
                .generalUserCount((long)personService.getAllPersonsDTO().size())
                .period(personService.getAllPersonsDTOByTimeBetween(BetweenDataRequest.builder()
                        .date1(LocalDate.now())
                        .date2(LocalDate.ofYearDay(LocalDate.now().getYear(),1))
                        .build())
                        .stream()
                        .map(personDTO -> personDTO.getCreatedOn().getMonth().name())
                        .collect(Collectors.toSet()))
                .usersCountByPeriod(new HashMap<>())
                .build();
        userStat.getPeriod().stream()
                .map(month -> userStat.getUsersCountByPeriod().put(month, (long)personService
                        .getAllPersonsDTOByTimeBetween(BetweenDataRequest.builder()
                                .date1(getFirstDayOfMonth(month))
                                .date2(getLastDayOfMonth(month))
                                .build())
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
                        .getAllPersonsDTOByTimeBetween(BetweenDataRequest.builder()
                                .date1(getFirstDayOfYear(year))
                                .date2(getLastDayOfYear(year))
                                .build())
                        .size()));
        return userStat;
    }

    private PostStatisticDTO buildPostStatsByMonth(){
        PostStatisticDTO postStat = PostStatisticDTO.builder()
                .generalPostCount((long)postService.getAllPosts().size())
                .period(postService.getAllPostsByTimeBetween(BetweenDataRequest.builder()
                        .date1(LocalDate.now())
                        .date2(LocalDate.ofYearDay(LocalDate.now().getYear(),1)).build())
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
                        .getAllPostsByTimeBetween(BetweenDataRequest.builder()
                                .date1(getFirstDayOfMonth(month))
                                .date2(getLastDayOfMonth(month))
                                .build())
                        .size()));
        return postStat;
    }

    private PostStatisticDTO buildPostStatsByAllTime(){
        PostStatisticDTO postStat = PostStatisticDTO.builder()
                .generalPostCount((long)postService.getAllPosts().size())
                .period(postService.getAllPosts()
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
                        .getAllPostsByTimeBetween(BetweenDataRequest.builder()
                                .date1(getFirstDayOfYear(year))
                                .date2(getLastDayOfYear(year))
                                .build())
                        .size()));
        return postStat;
    }

    private PostStatisticDTO buildPostStatsByWeek(){
        PostStatisticDTO postStat = PostStatisticDTO.builder()
                .generalPostCount((long)postService.getAllPosts().size())
                .period(postService
                        .getAllPostsByTimeBetween(BetweenDataRequest.builder()
                                .date1(getFirstDayOfMonth(LocalDate.now().getMonth().name()))
                                .date2(LocalDate.now())
                                .build())
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
                        .getAllPostsByTimeBetween(BetweenDataRequest.builder()
                                .date1(getFirstDayOfWeek(weekNumber))
                                .date2(getLastDayOfWeek(weekNumber))
                                .build())
                        .size()));
        return postStat;
    }

    private LocalDate getFirstDayOfWeek(String weekNumber){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR,Integer.valueOf(weekNumber));
        calendar.set(Calendar.YEAR, LocalDate.now().getYear());
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        Date date = calendar.getTime();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalDate getLastDayOfWeek(String weekNumber){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR,Integer.valueOf(weekNumber));
        calendar.set(Calendar.YEAR, LocalDate.now().getYear());
        calendar.set(Calendar.DAY_OF_WEEK, 7);
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
