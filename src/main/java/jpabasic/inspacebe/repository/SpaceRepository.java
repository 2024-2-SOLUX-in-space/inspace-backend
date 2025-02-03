package jpabasic.inspacebe.repository;

import jpabasic.inspacebe.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Integer> {

    @Query("SELECT s FROM Space s LEFT JOIN FETCH s.pages LEFT JOIN FETCH s.items WHERE s.sname LIKE %:query%")
    List<Space> findAllWithPagesAndItems(@Param("query") String query);

}
