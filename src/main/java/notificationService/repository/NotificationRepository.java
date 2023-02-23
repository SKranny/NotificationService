package notificationService.repository;

import notificationService.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByProfile_UserIdOrderByIsSent(Long personId, Pageable pageable);

    long countAllByProfile_UserId(Long personId);
}
