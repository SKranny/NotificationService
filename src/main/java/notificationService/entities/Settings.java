package notificationService.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "settings")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_id_gen")
    @SequenceGenerator(name = "notification_id_gen", sequenceName = "notification_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Builder.Default
    private Boolean friendRequest = false;

    @NotNull
    @Builder.Default
    private Boolean friendBirthday = false;

    @NotNull
    @Builder.Default
    private Boolean postComment = false;

    @NotNull
    @Builder.Default
    private Boolean commentOnComment = false;

    @NotNull
    @Builder.Default
    private Boolean post = false;

    @NotNull
    @Builder.Default
    private Boolean message = false;

    @NotNull
    @Builder.Default
    private Boolean phoneNotification = false;

    @NotNull
    @Builder.Default
    private Boolean emailNotification = false;
}
