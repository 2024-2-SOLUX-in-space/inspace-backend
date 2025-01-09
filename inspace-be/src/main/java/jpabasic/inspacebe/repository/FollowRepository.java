package jpabasic.inspacebe.repository;

import jpabasic.inspacebe.entity.Follow;
import jpabasic.inspacebe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, User> {
}
