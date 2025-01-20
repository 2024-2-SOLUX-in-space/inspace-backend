package jpabasic.inspacebe.repository;

import jpabasic.inspacebe.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Integer> {
    @Query("SELECT s FROM Space s WHERE s.spaceId = :spaceId")
    Space findSpaceById(@Param("spaceId") Integer spaceId);

    @Query("SELECT s FROM Space s JOIN FETCH s.pages WHERE s.spaceId = :spaceId")
    Space findSpaceWithPages(@Param("spaceId") Integer spaceId);

}
