package notificationService.services;

import dto.postDto.PostDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@FeignClient("post-service/api/v1/post")
public interface PostService {
    @GetMapping("/all")
    List<PostDTO> getAllPost();

    @GetMapping("/lastMonth")
    List<PostDTO> getAllPostByTimeBetween(LocalDate date1, LocalDate date2);

}
