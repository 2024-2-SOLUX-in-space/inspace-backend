<<<<<<< HEAD
package jpabasic.inspacebe.repository;

import jpabasic.inspacebe.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, String> {

    @Query("SELECT i FROM Item i WHERE i.title LIKE %:query% AND i.isUploaded = true")
    List<Item> findUploadedItems(@Param("query") String query);
}
=======
package jpabasic.inspacebe.repository;

import jpabasic.inspacebe.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
>>>>>>> e799886927a0899c21fc59810cc494b5cdd71bf4
