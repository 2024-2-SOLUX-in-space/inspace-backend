package jpabasic.inspacebe.repository;

import jpabasic.inspacebe.entity.ImageItem;
import jpabasic.inspacebe.entity.StickerItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StickerRepository extends CrudRepository<StickerItem, String> {
}
