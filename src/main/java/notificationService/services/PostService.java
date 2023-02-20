package notificationService.services;

import dto.postDto.PostDTO;
import feign.Param;
import notificationService.dto.Statistic.constant.BetweenDataRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient("post-service/api/v1/post")
public interface PostService {
    @GetMapping("/all")
    List<PostDTO> getAllPosts();

    @GetMapping("/allBetween")
    List<PostDTO> getAllPostsByTimeBetween(@RequestParam BetweenDataRequest request);

}
