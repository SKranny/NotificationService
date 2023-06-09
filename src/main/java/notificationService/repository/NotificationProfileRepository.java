package notificationService.repository;

import notificationService.entities.NotificationProfile;
import notificationService.exception.NotificationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface NotificationProfileRepository extends JpaRepository<NotificationProfile, Long> {
    Optional<NotificationProfile> findByUserId(Long userId);

    Set<NotificationProfile> findByUserIdIn(List<Long> idList);
}
