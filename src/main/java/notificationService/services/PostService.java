package notificationService.services;

import dto.postDto.PostDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@FeignClient("post-service/api/v1/post")
public interface PostService {
    @GetMapping("/all")
    List<PostDTO> getAllPosts();

    @GetMapping("/allBetween")
    List<PostDTO> getAllPostsByTimeBetween(LocalDate date1, LocalDate date2);

}
