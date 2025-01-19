package jpabasic.inspacebe.controller.notification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jpabasic.inspacebe.config.CurrentUser;
import jpabasic.inspacebe.dto.notification.NotificationResponseDto;
import jpabasic.inspacebe.entity.User;
import jpabasic.inspacebe.service.Notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
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
        List<NotificationResponseDto> response = notificationService.getUnreadNotifications(currentUser);

        // 읽음 처리
        notificationService.markAllAsRead(currentUser);

        return response;
    }
}
