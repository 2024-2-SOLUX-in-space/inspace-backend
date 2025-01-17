package jpabasic.inspacebe.service.item;

import jakarta.transaction.Transactional;
import jpabasic.inspacebe.dto.SpaceDetailResponseDto;
import jpabasic.inspacebe.dto.item.ArchiveRequestDto;
import jpabasic.inspacebe.dto.item.ItemRequestDto;
import jpabasic.inspacebe.dto.item.ItemResponseDto;
import jpabasic.inspacebe.dto.page.PageDto;
import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.entity.Page;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.entity.User;
import jpabasic.inspacebe.repository.ItemRepository;
import jpabasic.inspacebe.repository.PageRepository;
import jpabasic.inspacebe.repository.SpaceRepository;
import jpabasic.inspacebe.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import jpabasic.inspacebe.repository.SpaceRepository;
import jpabasic.inspacebe.service.search.SearchService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

public class ItemService {

    private final ItemRepository itemRepository;
    private final SpaceRepository spaceRepository;
    private final SearchService searchService;

    public ItemService(ItemRepository itemRepository, SearchService searchService, SpaceRepository spaceRepository) {
        this.itemRepository = itemRepository;
        this.searchService = searchService;
        this.spaceRepository = spaceRepository;
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
//        dto.setIsUploaded((Boolean) cacheData.get("isUploaded"));
        dto.setUserName("Crawled Source");
        return dto;
    }
    private ItemResponseDto convertToDto(Item item) {
        ItemResponseDto dto = new ItemResponseDto();
        dto.setItemId(item.getItemId());
        dto.setTitle(item.getTitle());
        dto.setImageUrl(item.getImageUrl());
//        dto.setIsUploaded(item.getIsUploaded());


        //민서 수정 //isUploaded=true 고르는 것이 아닌, "Page와 매핑이 되어 있는 경우"로 대체.
        if (item.getPageId() != null) {
            dto.setUserId(item.getUser().getUserId());
            dto.setUserName(item.getUser().getName());
        } else {
            dto.setUserName("Crawled Source");
        }

        return dto;
    }


    //아이템 저장소에서 삭제
    @Transactional
    public void deleteItemOnSpace(String itemId){
        itemRepository.deleteById(itemId);
    }

    //저장소 조회(카테고리별 아이템 전체 조회)
    @Transactional
    public ResponseEntity<List<ItemResponseDto>> getItemsBySpace (Integer spaceId) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Space not found"));
        List<Item> items=space.getItems();
        List<ItemResponseDto> dtos=PageDto.getItemList(items);
        return ResponseEntity.ok(dtos);
    }



    public SpaceDetailResponseDto getSpaceDetails(int spaceId) {
        // spaceId로 Space 조회
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공간입니다."));

        // Space 데이터를 DTO로 변환하여 반환
        return SpaceDetailResponseDto.fromEntity(space);
    }


}