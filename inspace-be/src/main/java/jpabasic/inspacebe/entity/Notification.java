package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 알림을 수신받는 당사자

    @ManyToOne
    @JoinColumn(name = "trigger_id")
    private User triggerId;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "is_read")
    private Boolean isRead;
}
