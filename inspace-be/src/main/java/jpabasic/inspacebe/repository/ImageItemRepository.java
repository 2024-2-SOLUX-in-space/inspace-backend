package jpabasic.inspacebe.repository;

import jpabasic.inspacebe.entity.ImageItem;
import org.springframework.data.repository.CrudRepository;

public interface ImageItemRepository extends CrudRepository<ImageItem, String> {
}
