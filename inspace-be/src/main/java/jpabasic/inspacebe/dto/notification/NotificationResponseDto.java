package jpabasic.inspacebe.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private String message;
    private Date createdAt;
}