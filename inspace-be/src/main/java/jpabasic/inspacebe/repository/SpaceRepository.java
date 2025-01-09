package jpabasic.inspacebe.repository;

import jpabasic.inspacebe.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Integer> {
}
