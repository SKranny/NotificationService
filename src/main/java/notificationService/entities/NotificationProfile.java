package notificationService.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "notification_profile")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_id_gen")
    @SequenceGenerator(name = "notification_id_gen", sequenceName = "notification_id_seq", allocationSize = 1)
    private Long id;

    @Builder.Default
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "setting_id", referencedColumnName = "id")
    private Settings settings = new Settings();

    private Long userId;

    @Builder.Default
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();
}
