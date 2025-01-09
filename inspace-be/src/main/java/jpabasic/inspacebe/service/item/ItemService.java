package jpabasic.inspacebe.service.item;

import jpabasic.inspacebe.dto.item.ItemRequestDto;
import jpabasic.inspacebe.dto.item.ItemResponseDto;
import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // 아이템 등록 로직
    public Item registerItem(ItemRequestDto itemRequestDto) {
        String randomId = UUID.randomUUID().toString();

        Item item = new Item();
        item.setItemId(randomId);
        item.setTitle(itemRequestDto.getTitle());
        item.setImageUrl(itemRequestDto.getImageUrl());
        item.setContentsUrl(itemRequestDto.getContentsUrl());
        item.setCtype(itemRequestDto.getCtype());
        item.setSpaceId(itemRequestDto.getSpaceId());
        item.setUid(itemRequestDto.getUid());

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
