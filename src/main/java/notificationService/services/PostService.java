package notificationService.services;

import dto.postDto.PostDTO;
import notificationService.dto.Statistic.constant.BetweenDataRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("post-service/api/v1/post")
public interface PostService {
    @GetMapping("/allPostList")
    List<PostDTO> getAllPosts();

    @GetMapping("/allPostPage")
    Page<PostDTO> getAllPosts(@RequestParam String searchedTitle);

    @GetMapping("/allPostBetween")
    List<PostDTO> getAllPostsByTimeBetween(@RequestParam BetweenDataRequest request);
    
    @GetMapping("/allBlocked")
    List<PostDTO> getAllPostsByIsBlockedIsTrue();

    @GetMapping("/getActive")
    List<PostDTO> getAllPostsByIsBlockedIsFalseAndByIsDeletedIsFalse();
}
