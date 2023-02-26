package notificationService.services;

import dto.postDto.PostDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminPanelService {
    private final PostService postService;

    public Page<PostDTO> getAllPosts(String searchedTitle, Integer page){
        return postService.getAllPosts(searchedTitle,page);
    }

    public Page<PostDTO> getActivePosts(String searchedTitle, Integer page) {
        return postService.getAllActivePosts(searchedTitle, page);
    }

    public Page<PostDTO> getBlockedPosts(String searchedTitle, Integer page) {
        return postService.getAllPostsByIsBlockedIsTrue(searchedTitle, page);
    }
}
