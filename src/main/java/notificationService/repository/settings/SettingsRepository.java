package notificationService.repository.settings;

import notificationService.dto.notification.SettingsFilter;
import notificationService.entities.Settings;
import notificationService.repository.settings.custom.SettingsSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long>, JpaSpecificationExecutor<Settings> {
    default Optional<Settings> findSettingsProfile(SettingsFilter filter) {
        Specification<Settings> specification = Specification
                .where(SettingsSpecifications.isFriendRequest(filter.isFriendRequest())
                        .and(SettingsSpecifications.isFriendBirthday(filter.isFriendBirthday()))
                        .and(SettingsSpecifications.isCommentOnComment(filter.isCommentComment()))
                        .and(SettingsSpecifications.isMessage(filter.isMessage()))
                        .and(SettingsSpecifications.isPostComment(filter.isPostComment()))
                        .and(SettingsSpecifications.isSendPhoneMessage(filter.isSendPhoneMessage()))
                        .and(SettingsSpecifications.isSendEmailMessage(filter.isSendEmailMessage()))
                        .and(SettingsSpecifications.isPost(filter.isPost())));

        return this.findOne(specification);
    }
}
