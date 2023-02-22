package notificationService.controller;

import dto.postDto.PostDTO;
import lombok.RequiredArgsConstructor;
import notificationService.services.AdminPanelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications/admin-panel")
@RequiredArgsConstructor
public class AdminPanelController {
    private final AdminPanelService adminPanelService;

    @GetMapping("/all")
    public List<PostDTO> getAllPosts(){
        return adminPanelService.getAllPosts();
    }

    @GetMapping("/active")
    public List<PostDTO> getActivePosts(){
        return adminPanelService.getActivePosts();
    }

    @GetMapping("/blocked")
    public List<PostDTO> getBlockedPosts(){
        return adminPanelService.getBlockedPosts();
    }
}
