package notificationService.repository;

import notificationService.entities.NotificationProfile;
import notificationService.exception.NotificationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationProfileRepository extends JpaRepository<NotificationProfile, Long> {
    Optional<NotificationProfile> findByUserId(Long userId);

    Optional<List<NotificationProfile>> findByUserIdIn(List<Long> idList);

    default Integer countNotificationsByUserId(Long userId) {
        return findByUserId(userId)
                .map(NotificationProfile::getNotifications)
                .map(List::size)
                .orElseThrow(() -> new NotificationException("Error! Unknown person!", HttpStatus.UNAUTHORIZED));
    }
}
