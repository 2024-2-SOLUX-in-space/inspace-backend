package jpabasic.inspacebe.dto.item;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserImageDto {

    private Integer spaceId;
    private String title;

}
