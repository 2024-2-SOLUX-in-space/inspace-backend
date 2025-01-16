package jpabasic.inspacebe.service.item;

import jpabasic.inspacebe.dto.item.ArchiveRequestDto;
import jpabasic.inspacebe.dto.item.ItemRequestDto;
import jpabasic.inspacebe.dto.item.ItemResponseDto;
import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.entity.Page;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.repository.ItemRepository;
import jpabasic.inspacebe.repository.PageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final PageRepository pageRepository;

    public ItemService(ItemRepository itemRepository, PageRepository pageRepository) {
        this.itemRepository = itemRepository;
        this.pageRepository=pageRepository;
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

    //아이템 페이지(아카이브)에 등록 //pageNum 수정 필요.
    public void archiveItems (Integer pageId,List<ArchiveRequestDto> dtoList) {

        Page page= pageRepository.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Page not found with id: " + pageId));


        for(ArchiveRequestDto dto : dtoList) {
            Item item= ArchiveRequestDto.toEntity(dto);
            itemRepository.save(item);
            item.setPage(page);
        }


    }
}
