package jpabasic.inspacebe.repository;

import jpabasic.inspacebe.entity.Notification;
import jpabasic.inspacebe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 읽지 않은 알림만 조회
    List<Notification> findByReceiverAndIsReadFalseOrderByCreatedAtDesc(User receiver);
}