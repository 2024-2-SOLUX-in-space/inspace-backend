package jpabasic.inspacebe.service.item;

import jakarta.transaction.Transactional;
import jpabasic.inspacebe.dto.SpaceDetailResponseDto;
import jpabasic.inspacebe.dto.item.ItemRequestDto;
import jpabasic.inspacebe.dto.item.ItemResponseDto;
import jpabasic.inspacebe.dto.page.PageDto;
import jpabasic.inspacebe.entity.CType;
import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.repository.ItemRepository;
import jpabasic.inspacebe.repository.PageRepository;
import jpabasic.inspacebe.repository.SpaceRepository;
import jpabasic.inspacebe.repository.UserRepository;
import jpabasic.inspacebe.service.search.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service

public class ItemService {

    private final ItemRepository itemRepository;
    private final SpaceRepository spaceRepository;
    private final SearchService searchService;
    private final UserRepository userRepository;
    private final PageRepository pageRepository;

    public ItemService(ItemRepository itemRepository, SearchService searchService, SpaceRepository spaceRepository, UserRepository userRepository, PageRepository pageRepository) {
        this.itemRepository = itemRepository;
        this.searchService = searchService;
        this.spaceRepository = spaceRepository;
        this.userRepository = userRepository;
        this.pageRepository = pageRepository;
    }


    public ItemResponseDto getItemDetails(String itemId) {
        return searchService.getCrawledItemCache(itemId)
                .map(this::convertCacheToDto)
                .orElseGet(() -> {
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

    //아이템 등록
    @Transactional
    public void registerItem(ItemRequestDto itemRequestDto, String query) {
        // 필수 입력값 검증
        if (itemRequestDto.getItemId() == null || itemRequestDto.getUid() == null ||
                itemRequestDto.getSpaceId() == null || itemRequestDto.getPageId() == null) {
            throw new IllegalArgumentException("ItemId, Uid, SpaceId, PageId는 필수 입력값입니다.");
        }

        // Space와 User 검증
        var space = spaceRepository.findById(itemRequestDto.getSpaceId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Space ID"));
        var user = userRepository.findById(itemRequestDto.getUid())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));

        // 디버깅을 위한 추가 로그
        System.out.println("User ID from DTO: " + itemRequestDto.getUid());
        System.out.println("Fetched User: " + user);

        // Page 검증 및 설정
        var page = pageRepository.findById(itemRequestDto.getPageId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Page ID"));


        if (page.getPageNumber() == 0) {
            page.setPageNumber(1); // 기본값 설정
        }
        // Page가 Space와 매핑되어 있는지 검증
        if (!space.getPages().contains(page)) {
            throw new IllegalArgumentException("The provided Page does not belong to the specified Space.");
        }

        Item item;

        // DB에서 아이템 확인
        var existingItem = itemRepository.findById(itemRequestDto.getItemId());

        if (existingItem.isPresent()) {
            // 이미 업로드된 아이템
            item = existingItem.get();
            item.setPage(page);
            item.setSpace(space);
            item.setUser(user);
        } else {
            // 캐시에서 아이템 확인
            var cachedData = searchService.getCrawledItemCache(itemRequestDto.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("캐시에 존재하지 않는 아이템입니다."));

            item = new Item();
            Boolean isUploaded = (Boolean) cachedData.get("isUploaded");
            if (isUploaded != null && isUploaded) {
                item.setTitle((String) cachedData.get("title"));
            } else {
                item.setTitle(query);
            }

            item.setCtype(CType.fromValue((String) cachedData.get("ctype")));
            item.setImageUrl((String) cachedData.get("imageUrl"));
            item.setContentsUrl((String) cachedData.get("contentsUrl"));
            item.setIsUploaded(false);
            item.setSpace(space);
            item.setPage(page); // Page 설정
            item.setUser(user);

            searchService.removeCachedItem(itemRequestDto.getItemId());
        }

        itemRepository.save(item); // DB에 영구 저장
    }


    //아이템 저장소에서 삭제
    @Transactional
    public void deleteItemOnSpace(String itemId) {
        itemRepository.deleteById(itemId);
    }

    //저장소 조회(카테고리별 아이템 전체 조회)
    @Transactional
    public ResponseEntity<List<ItemResponseDto>> getItemsBySpace(Integer spaceId) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Space not found"));
        List<Item> items = space.getItems();
        List<ItemResponseDto> dtos = PageDto.getItemList(items);
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