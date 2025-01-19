package jpabasic.inspacebe.controller.notification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jpabasic.inspacebe.config.CurrentUser;
import jpabasic.inspacebe.dto.notification.NotificationResponseDto;
import jpabasic.inspacebe.entity.User;
import jpabasic.inspacebe.service.Notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "Notification", description = "알림 API")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "SSE 스트림 구독", description = "SSE를 이용하여 실시간 알림을 전송합니다. Swagger에선 테스트가 불가하니 postman을 이용하세요")
    @GetMapping(path = "/stream")
    public SseEmitter streamNotifications(@CurrentUser User currentUser) {
        return notificationService.createEmitter(currentUser.getUserId());
    }

    // 읽지 않은 알림 목록 조회
    @Operation(summary = "읽지 않은 알림 조회", description = "읽지 않은 알림 목록을 반환합니다.")
    @GetMapping("/unread")
    public List<NotificationResponseDto> getUnreadNotifications(@CurrentUser User currentUser) {
        return notificationService.getUnreadNotifications(currentUser);
    }

    @Operation(summary = "모두 읽음 처리", description = "읽지 않은 모든 알림을 읽음 처리합니다.")
    @PatchMapping("/read-all")
    public ResponseEntity<String> markAllNotificationsAsRead(@CurrentUser User currentUser) {
        // 읽음 처리
        notificationService.markAllAsRead(currentUser);
        return ResponseEntity.ok("모두 읽음 처리"); // 204 No Content 반환
    }

    @Operation(summary = "읽음 처리", description = "특정 알림을 읽음 처리합니다.")
    @PatchMapping("/read/{id}")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("읽음 처리"); // 성공 시 204 No Content 반환
    }
}
