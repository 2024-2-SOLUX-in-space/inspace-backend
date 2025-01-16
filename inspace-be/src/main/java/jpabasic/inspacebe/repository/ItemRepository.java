package jpabasic.inspacebe.repository;

import jpabasic.inspacebe.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, String> {

    @Query("SELECT i FROM Item i WHERE i.title LIKE %:query% AND i.isUploaded = true")
    List<Item> findUploadedItems(@Param("query") String query);

}
