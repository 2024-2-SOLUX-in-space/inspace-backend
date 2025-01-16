package jpabasic.inspacebe.service.item;

import jakarta.transaction.Transactional;
import jpabasic.inspacebe.dto.item.ArchiveRequestDto;
import jpabasic.inspacebe.dto.item.ItemRequestDto;
import jpabasic.inspacebe.dto.item.ItemResponseDto;
import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.entity.Page;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.entity.User;
import jpabasic.inspacebe.repository.ItemRepository;
import jpabasic.inspacebe.repository.PageRepository;
import jpabasic.inspacebe.repository.SpaceRepository;
import jpabasic.inspacebe.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

public class ItemService {

    private final ItemRepository itemRepository;
    private final SpaceRepository spaceRepository;

    public ItemService(ItemRepository itemRepository, PageRepository pageRepository, SpaceRepository spaceRepository) {
        this.itemRepository = itemRepository;
//        this.pageRepository=pageRepository;
        this.spaceRepository = spaceRepository;

    }

    // 아이템 등록 로직 //민서 수정
    @Transactional
    public Item registerItem(ItemRequestDto itemRequestDto) {
        String randomId = UUID.randomUUID().toString();

        Space space = spaceRepository.findById(itemRequestDto.getSpaceId())
                .orElseThrow(() -> new IllegalArgumentException("Space not found"));

//        User user = userRepository.findById(itemRequestDto.getUid())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//

        Item item = new Item();
        item.setItemId(randomId);
        item.setTitle(itemRequestDto.getTitle());
        item.setImageUrl(itemRequestDto.getImageUrl());
        item.setContentsUrl(itemRequestDto.getContentsUrl());
        item.setCtype(itemRequestDto.getCtype());
//        item.setSpace(space);
//        item.setUser(user);

        return itemRepository.save(item);
    }


    // 아이템 상세 조회//민서 수정
    @Transactional
    public ItemResponseDto getItemDetails(String itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        ItemResponseDto responseDto = new ItemResponseDto();
        responseDto.setItemId(item.getItemId());
        responseDto.setTitle(item.getTitle());
        responseDto.setImageUrl(item.getImageUrl());
        responseDto.setContentsUrl(item.getContentsUrl());
        responseDto.setCtype(item.getCtype());
//        responseDto.setSpaceId(item.getSpaceId());
//        responseDto.setUid(item.getUid());

        return responseDto;
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
        List<ItemResponseDto> responseDtos=items.stream()
                .map(ItemResponseDto::toDto)//각 Item을 ItemResponseDto로 변환.
                .collect(Collectors.toList()); //결과를 리스트로 수집
        return ResponseEntity.ok(responseDtos);
    }

}
