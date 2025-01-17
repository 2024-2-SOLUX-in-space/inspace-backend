package jpabasic.inspacebe.service.item;

import jakarta.transaction.Transactional;
import jpabasic.inspacebe.dto.SpaceDetailResponseDto;
import jpabasic.inspacebe.dto.item.ArchiveRequestDto;
import jpabasic.inspacebe.dto.item.ItemRequestDto;
import jpabasic.inspacebe.dto.item.ItemResponseDto;
import jpabasic.inspacebe.dto.item.UserImageDto;
import jpabasic.inspacebe.dto.page.PageDto;
import jpabasic.inspacebe.entity.*;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.repository.ItemRepository;
import jpabasic.inspacebe.repository.PageRepository;
import jpabasic.inspacebe.repository.SpaceRepository;
import jpabasic.inspacebe.repository.UserRepository;
import jpabasic.inspacebe.service.StorageService;
import org.springframework.http.ResponseEntity;
import jpabasic.inspacebe.repository.SpaceRepository;
import jpabasic.inspacebe.service.search.SearchService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final StorageService storageService;

    public ItemService(ItemRepository itemRepository, SearchService searchService, SpaceRepository spaceRepository,StorageService storageService) {
        this.itemRepository = itemRepository;
        this.searchService = searchService;
        this.spaceRepository = spaceRepository;
        this.storageService = storageService;
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
        dto.setIsUploaded(item.getIsUploaded());

        if (item.getIsUploaded()) {
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
    public ResponseEntity<List<ItemResponseDto>> getItemsBySpace (Integer spaceId,String category) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Space not found"));

        //필터링  해야한다.
        if(category.equals("userImage")){
            space.getItems().stream();
        }
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

    //유저의 사진을 클라우드 스토리지에 업로드 -> 클라우드 저장 경로를 db에 저장
    public String uploadImageAndSaveTodb(MultipartFile file, Integer spaceId,String title){



        Space space=spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Space not found"));

        /// Firebase에 파일 업로드
        String fileUrl=storageService.uploadImage(file);

        /// 데이터베이스에 경로 저장
        Item item=new Item();
        item.setTitle(title);
        item.setIsUploaded(true); //유저가 직접 올린 이미지=true
        item.setImageUrl(fileUrl);
        item.setSpace(space);
        item.setCtype(CType.IMAGE);
        itemRepository.save(item);

        return fileUrl;

    }



}