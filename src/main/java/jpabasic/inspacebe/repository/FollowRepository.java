package jpabasic.inspacebe.repository;

import jpabasic.inspacebe.entity.Follow;
import jpabasic.inspacebe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowingAndFollowed(User following, User followed);
    List<Follow> findAllByFollowing(User following);
    List<Follow> findAllByFollowed(User followed);
}
