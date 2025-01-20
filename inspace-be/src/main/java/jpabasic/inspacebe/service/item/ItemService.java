package jpabasic.inspacebe.service.item;

import jakarta.servlet.Filter;
import jakarta.transaction.Transactional;
import jpabasic.inspacebe.dto.SpaceDetailResponseDto;
import jpabasic.inspacebe.dto.item.ItemRequestDto;
import jpabasic.inspacebe.dto.item.ItemResponseDto;
import jpabasic.inspacebe.dto.page.PageDto;
import jpabasic.inspacebe.entity.CType;
import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.dto.item.*;
import jpabasic.inspacebe.entity.*;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.repository.ItemRepository;
import jpabasic.inspacebe.repository.PageRepository;
import jpabasic.inspacebe.repository.SpaceRepository;
import jpabasic.inspacebe.repository.UserRepository;
import jpabasic.inspacebe.service.StorageService;
import org.springframework.http.ResponseEntity;
import jpabasic.inspacebe.service.search.SearchService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final SpaceRepository spaceRepository;
    private final SearchService searchService;
    private final UserRepository userRepository;
    private final PageRepository pageRepository;

    private final StorageService storageService;


    public ItemService(ItemRepository itemRepository, SearchService searchService, SpaceRepository spaceRepository, UserRepository userRepository, PageRepository pageRepository,  StorageService storageService) {
        this.itemRepository = itemRepository;
        this.searchService = searchService;
        this.spaceRepository = spaceRepository;
        this.userRepository = userRepository;
        this.pageRepository = pageRepository;
        this.storageService = storageService;
    }





    @Transactional
    public ItemResponseDto getItemDetails(String itemId) {
        return searchService.getCrawledItemCache(itemId)
                .map(this::convertCacheToDto)
                .orElseGet(() -> {
                    Item item = itemRepository.findById(itemId)
                            .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));
                    return convertToDto(item);
                });
    }


    @Transactional //민서 수정 private->public
    public ItemResponseDto convertCacheToDto(Map<String, Object> cacheData) {
        ItemResponseDto dto = new ItemResponseDto();
        dto.setItemId((String) cacheData.get("itemId"));
        dto.setTitle((String) cacheData.get("title"));
        dto.setImageUrl((String) cacheData.get("url"));
        dto.setIsUploaded((Boolean) cacheData.get("isUploaded"));
        dto.setUserName("Crawled Source");
        return dto;
    }


=======
    @Transactional //민서 수정 private -> public
    public ItemResponseDto convertToDto(Item item) {
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
    public void deleteItemOnSpace(String itemId){
        Item item=itemRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));
        if(item.getIsUploaded()){
            storageService.deleteFile(item.getContentsUrl());
        }
        itemRepository.deleteById(itemId);
    }

    @Transactional
    public ResponseEntity<List<ItemsDto>> getItemsBySpace(Integer spaceId, String category) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Space not found"));

        List<Item> items = (List<Item>) space.getItems(); // 반복적으로 호출되므로 미리 한 번 가져옵니다.
        List<Item> filteredItems = new ArrayList<>();

        if (category.equals("USERIMAGE")) {
            filteredItems = items.stream()
                    .filter(item -> item.getIsUploaded() == true) // isUploaded가 true인 항목만 필터링
                    .collect(Collectors.toList());

        } else if (category.equals("YOUTUBE")) {
            filteredItems = items.stream()
                    .filter(item -> item.getCtype() == CType.YOUTUBE)
                    .collect(Collectors.toList());

        } else if (category.equals("MUSIC")) {
            filteredItems = items.stream()
                    .filter(item -> item.getCtype() == CType.MUSIC)
                    .collect(Collectors.toList());

        } else { // 기본적으로 IMAGE 카테고리
            filteredItems = items.stream()
                    .filter(item -> item.getCtype() == CType.IMAGE)
                    .filter(item -> !item.getIsUploaded())
                    .collect(Collectors.toList());
        }

        // Item -> ItemResponseDto 변환 (DTO로 수정)
        List<ItemsDto> dtoList = filteredItems.stream()
                .map(ItemsDto::toDto) // Item을 ItemResponseDto로 변환하는 메서드 호출
                .collect(Collectors.toList());

        // 또는 pageDto를 사용하려면:
        // List<ItemResponseDto> dtos = PageDto.getItemList(filteredItems);

        return ResponseEntity.ok(dtoList); // 변환된 DTO 목록 반환
    }



    @Transactional
    public SpaceDetailResponseDto getSpaceDetails(int spaceId) {
        // spaceId로 Space 조회
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공간입니다."));

        // Space 데이터를 DTO로 변환하여 반환
        return SpaceDetailResponseDto.fromEntity(space);
    }



    @Transactional
    //유저의 사진을 클라우드 스토리지에 업로드 -> 클라우드 저장 경로를 db에 저장
    public String uploadImageAndSaveTodb(MultipartFile file, Integer spaceId,String title){
        Space space=spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Space not found"));

        Item item=new Item();

        /// Firebase에 파일 업로드
        Map<String, String> result=storageService.uploadImage(file,item.getItemId());

        // 각각의 값 추출
        String randomUUID = result.get("randomUUID");
        String imageUrl = result.get("imageUrl");



        /// 데이터베이스에 경로 저장
        String contentsUrl=storageService.getImagePath(file,randomUUID);

        item.setTitle(title);
        item.setIsUploaded(true); //유저가 직접 올린 이미지=true
        item.setImageUrl(imageUrl.toString());
        item.setSpace(space);
        item.setContentsUrl(contentsUrl);//삭제 시 필요한 파일 경로
        item.setCtype(CType.IMAGE);
        item.setUid(space.getUser().getUserId());
        itemRepository.save(item);

        return imageUrl;

    }






}