package notificationService.services;

import dto.postDto.PostDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminPanelService {
    private final PostService postService;

    public List<PostDTO> getAllPosts(){
        return postService.getAllPosts();
    }

    public List<PostDTO> getActivePosts() {
        return postService.getAllPostsByIsBlockedIsFalseAndByIsDeletedIsFalse();
    }

    public List<PostDTO> getBlockedPosts() {
        return postService.getAllPostsByIsBlockedIsTrue();
    }
}
