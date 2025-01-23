package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User receiver; // 알림을 수신받는 당사자

    @Column(name = "message", nullable = false)
    private String message; // 알림 메시지 필드 추가

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "is_read")
    private Boolean isRead;
}
