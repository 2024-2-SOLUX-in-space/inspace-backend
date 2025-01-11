package jpabasic.inspacebe.service.item;

import jpabasic.inspacebe.dto.item.ConfirmItemRequest;
import jpabasic.inspacebe.dto.item.ItemRequestDto;
import jpabasic.inspacebe.dto.item.ItemResponseDto;
import jpabasic.inspacebe.dto.item.TemporaryItemDto;
import jpabasic.inspacebe.entity.CType;
import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService {

    private final Map<String, TemporaryItemDto> temporaryItems = new HashMap<>();
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }



    // 검색 및 임시 데이터 생성
    public List<TemporaryItemDto> searchItems(String query) {
        // 크롤링 후 임시 데이터 생성 (예제 데이터)
        List<TemporaryItemDto> results = new ArrayList<>();
        TemporaryItemDto item = new TemporaryItemDto(
                UUID.randomUUID().toString(), "Sample Title", "image_url", "contents_url"
        );
        temporaryItems.put(item.getUuid(), item); // 메모리에 저장
        results.add(item);
        return results;
    }

    // 임시 데이터 상세 조회
    public TemporaryItemDto getTemporaryItem(String uuid) {
        return temporaryItems.get(uuid);
    }

    // 아이템 저장 (확정)
    public Item addItem(ConfirmItemRequest request) {
        // 임시 데이터를 확정 아이템으로 변환
        TemporaryItemDto tempItem = temporaryItems.get(request.getUuid());
        if (tempItem == null) {
            throw new IllegalArgumentException("Invalid UUID");
        }

        Item item = new Item();
        item.setTitle(tempItem.getTitle());
        item.setImageUrl(tempItem.getImageUrl());
        item.setContentsUrl(tempItem.getContentsUrl());
        item.setCtype(CType.fromValue(request.getCType()));
        item.setUid(request.getUid());
        item.setSpaceId(request.getSpaceId());

        // DB에 저장
        return itemRepository.save(item);
    }


    // 아이템 상세 조회
    public ItemResponseDto getItemDetails(String itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        ItemResponseDto responseDto = new ItemResponseDto();
        responseDto.setItemId(item.getItemId());
        responseDto.setTitle(item.getTitle());
        responseDto.setImageUrl(item.getImageUrl());
        responseDto.setContentsUrl(item.getContentsUrl());
        responseDto.setCtype(item.getCtype());
        responseDto.setSpaceId(item.getSpaceId());
        responseDto.setUid(item.getUid());

        return responseDto;
    }
}
