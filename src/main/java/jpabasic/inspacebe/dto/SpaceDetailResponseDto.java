package jpabasic.inspacebe.dto;

import jpabasic.inspacebe.entity.Space;

import java.time.LocalDateTime;

public class SpaceDetailResponseDto {
    private final int spaceId;
    private final String sname;
    private final boolean isPublic;
    private final boolean isPrimary;
    private final LocalDateTime createdAt;
    private final int userId;
    private final String userName;

    // 생성자
    public SpaceDetailResponseDto(int spaceId, String sname, boolean isPublic, boolean isPrimary,
                                  LocalDateTime createdAt, int userId, String userName) {
        this.spaceId = spaceId;
        this.sname = sname;
        this.isPublic = isPublic;
        this.isPrimary = isPrimary;
        this.createdAt = createdAt;
        this.userId = userId;
        this.userName = userName;
    }

    // fromEntity 메서드
    public static SpaceDetailResponseDto fromEntity(Space space) {
        return new SpaceDetailResponseDto(
                space.getSpaceId(),
                space.getSname(),
                space.getIsPublic(),
                space.getIsPrimary(),
                space.getCreatedAt(),
                space.getUser().getUserId(),
                space.getUser().getName()
        );
    }

    // Getters
    public int getSpaceId() {
        return spaceId;
    }

    public String getSname() {
        return sname;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
