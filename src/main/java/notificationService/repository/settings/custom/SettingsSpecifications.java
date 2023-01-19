package notificationService.repository.settings.custom;

import notificationService.entities.Settings;
import org.springframework.data.jpa.domain.Specification;

public class SettingsSpecifications {
    public static Specification<Settings> isFriendRequest(Boolean friendRequest) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("friendRequest"), friendRequest);
    }

    public static Specification<Settings> isFriendBirthday(Boolean friendBirthday) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("friendBirthday"), friendBirthday);
    }

    public static Specification<Settings> isPostComment(Boolean postComment) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("postComment"), postComment));
    }

    public static Specification<Settings> isCommentOnComment(Boolean commentComment) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("commentOnComment"), commentComment);
    }

    public static Specification<Settings> isPost(Boolean post) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("post"), post);
    }

    public static Specification<Settings> isMessage(Boolean message) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("message"), message);
    }

    public static Specification<Settings> isSendPhoneMessage(Boolean sendPhoneMessage) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("phoneNotification"), sendPhoneMessage);
    }

    public static Specification<Settings> isSendEmailMessage(Boolean sendEmailMessage) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("emailNotification"), sendEmailMessage);
    }
}
