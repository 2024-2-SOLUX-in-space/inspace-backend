package jpabasic.inspacebe.repository;

import jpabasic.inspacebe.entity.ImageItem;
import jpabasic.inspacebe.entity.StickerItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StickerRepository extends JpaRepository<StickerItem, String> {
    boolean existsByTitle(String title);
    StickerItem findByTitle(String title);
}
