package notificationService.controller;

import dto.postDto.PostDTO;
import lombok.RequiredArgsConstructor;
import notificationService.services.AdminPanelService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications/admin-panel")
@RequiredArgsConstructor
public class AdminPanelController {
    private final AdminPanelService adminPanelService;

    @GetMapping("/all")
    public Page<PostDTO> getAllPosts(@RequestParam String searchedTitle, @RequestParam Integer page){
        return adminPanelService.getAllPosts(searchedTitle, page);
    }

    @GetMapping("/active")
    public Page<PostDTO> getActivePosts(@RequestParam String searchedTitle, @RequestParam Integer page){
        return adminPanelService.getActivePosts(searchedTitle,page);
    }

    @GetMapping("/blocked")
    public Page<PostDTO> getBlockedPosts(@RequestParam String searchedTitle, @RequestParam Integer page){
        return adminPanelService.getBlockedPosts(searchedTitle, page);
    }
}
