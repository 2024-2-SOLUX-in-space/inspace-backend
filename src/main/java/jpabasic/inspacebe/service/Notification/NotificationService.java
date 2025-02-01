package jpabasic.inspacebe.service.Notification;

import jakarta.transaction.Transactional;
import jpabasic.inspacebe.dto.notification.NotificationResponseDto;
import jpabasic.inspacebe.entity.Notification;
import jpabasic.inspacebe.entity.User;
import jpabasic.inspacebe.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final Map<Integer, SseEmitter> userEmitters = new ConcurrentHashMap<>();
    private final NotificationRepository notificationRepository;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    // SSE 구독 생성
    public SseEmitter createEmitter(Integer userId) {
        SseEmitter emitter = new SseEmitter(3600L * 1000L);
        userEmitters.put(userId, emitter);

        emitter.onCompletion(() -> userEmitters.remove(userId));
        emitter.onTimeout(() -> userEmitters.remove(userId));
        emitter.onError((e) -> userEmitters.remove(userId));

        return emitter;
    }

    @Scheduled(fixedRate = 5000) // 5초마다 실행
    public void sendHeartbeat() {
        userEmitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("heartbeat").data("ping"));
            } catch (IOException e) {
                emitter.complete();
                userEmitters.remove(userId);
            }
        });
    }

    // 팔로우 알림 전송
    @Transactional
    public void sendFollowNotification(User receiver, String message) {
        // 알림 데이터 생성 및 저장
        Notification notification = Notification.builder()
                .receiver(receiver)
                .message(message)
                .isRead(false)
                .createdAt(new Date())
                .build();

        notificationRepository.save(notification);

        // 실시간 알림 전송
        sendRealTimeNotification(receiver.getUserId(), notification);
    }

    private void sendRealTimeNotification(Integer userId, Notification notification) {
        SseEmitter emitter = userEmitters.get(userId);
        if (emitter != null) {
            executor.execute(() -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name("notification")
                            .data(Map.of(
                                    "message", notification.getMessage(),
                                    "isRead", notification.getIsRead()
                            )));
                } catch (IOException e) {
                    log.error("Failed to send notification to userId: " + userId, e);
                    emitter.complete();
                    userEmitters.remove(userId);
                }
            });
        } else {
            log.warn("No active SSE connection for userId: " + userId + ". Notification saved but not delivered in real-time.");
        }
    }


    // 읽지 않은 알림 목록 조회
    @Transactional
    public List<NotificationResponseDto> getUnreadNotifications(User receiver) {
        return notificationRepository.findByReceiverAndIsReadFalseOrderByCreatedAtDesc(receiver).stream()
                .map(notification -> new NotificationResponseDto(
                        notification.getId(),
                        notification.getMessage(),
                        notification.getCreatedAt()
                ))
                .toList();
    }

    // 알림 읽음 처리
    @Transactional
    public void markAllAsRead(User receiver) {
        List<Notification> unreadNotifications = notificationRepository.findByReceiverAndIsReadFalseOrderByCreatedAtDesc(receiver);
        unreadNotifications.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }
    @Transactional
    public void markAsRead(Long notificationId) {
        // 1. 알림 조회
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 알림을 찾을 수 없습니다. ID: " + notificationId));

        // 2. 읽음 처리
        notification.setIsRead(true);

        // 3. 저장
        notificationRepository.save(notification);
    }
}
