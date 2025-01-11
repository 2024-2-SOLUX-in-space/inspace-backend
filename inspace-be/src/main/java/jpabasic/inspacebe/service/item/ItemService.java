package jpabasic.inspacebe.service.item;


import jpabasic.inspacebe.dto.item.ItemResponseDto;
import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.repository.ItemRepository;
import jpabasic.inspacebe.service.search.SearchService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final SearchService searchService;

    public ItemService(ItemRepository itemRepository, SearchService searchService) {
        this.itemRepository = itemRepository;
        this.searchService = searchService;
    }

    public ItemResponseDto getItemDetails(String itemId) {
        // 캐시에서 데이터 검색
        return searchService.getCachedItem(itemId)
                .map(this::convertCacheToDto)
                .orElseGet(() -> {
                    // DB에서 조회
                    Item item = itemRepository.findById(itemId)
                            .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));
                    return convertToDto(item);
                });
    }

    private ItemResponseDto convertCacheToDto(Map<String, Object> cacheData) {
        ItemResponseDto dto = new ItemResponseDto();
        dto.setItemId((String) cacheData.get("itemId"));
        dto.setTitle((String) cacheData.get("title"));
        dto.setImageUrl((String) cacheData.get("url"));
        dto.setIsUploaded((Boolean) cacheData.get("isUploaded"));
        dto.setUserName("Crawled Source");
        return dto;
    }

    private ItemResponseDto convertToDto(Item item) {
        ItemResponseDto dto = new ItemResponseDto();
        dto.setItemId(item.getItemId());
        dto.setTitle(item.getTitle());
        dto.setImageUrl(item.getImageUrl());
        dto.setIsUploaded(item.getIsUploaded());

        if (item.getIsUploaded()) {
            dto.setUserId(item.getUser().getUserId());
            dto.setUserName(item.getUser().getName());
        } else {
            dto.setUserName("Crawled Source");
        }

        return dto;
    }
}