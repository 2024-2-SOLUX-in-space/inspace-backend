package jpabasic.inspacebe.repository;

import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, String> {

    @Query("SELECT i FROM Item i WHERE i.title LIKE %:query% AND i.isUploaded = true")
    List<Item> findUploadedItems(@Param("query") String query);

    Optional<Item> findByTitle(String title);

}
