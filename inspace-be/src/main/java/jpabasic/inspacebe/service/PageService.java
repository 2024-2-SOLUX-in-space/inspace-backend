package jpabasic.inspacebe.service;

import jpabasic.inspacebe.dto.item.ArchiveRequestDto;
import jpabasic.inspacebe.dto.page.PageDto;
import jpabasic.inspacebe.entity.Item;
import jpabasic.inspacebe.entity.Page;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.entity.User;
import jpabasic.inspacebe.repository.ItemRepository;
import jpabasic.inspacebe.repository.PageRepository;
import jpabasic.inspacebe.repository.SpaceRepository;
import jpabasic.inspacebe.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {

    private final SpaceRepository spaceRepository;
    private final ItemRepository itemRepository;
    private final PageRepository pageRepository;
    private final UserRepository userRepository;

    public PageService(SpaceRepository spaceRepository, ItemRepository itemRepository, PageRepository pageRepository, UserRepository userRepository) {
        this.spaceRepository = spaceRepository;
        this.itemRepository = itemRepository;
        this.pageRepository = pageRepository;
        this.userRepository = userRepository;
    }

    //페이지 생성
    public void createPages(Integer spaceId,Integer userId){

        Space space=spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Space not found"));
        User user=userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        for(int i=0;i<10;i++){
            Page page=new Page();
            page.setPageNumber(i+1);
            page.setSpace(space);
            page.setUser(user);
            pageRepository.save(page);
        }



    }

    //페이지 조회
    public PageDto getPage(Integer spaceId, Integer pageNumber) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Space not found"));

        List<Page> pages = space.getPages();
        Page targetPage = null;
        for (Page target : pages) {
            if (target.getPageNumber() == pageNumber) {
                targetPage = target;
                break;
            }
        }

        PageDto dto = PageDto.toDto(targetPage);
        return dto;

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

    //페이지(아카이브)에서 아이템 삭제
    public void deleteItemOnPage(String itemId) {
        Item item=itemRepository.findById(itemId)
                .orElseThrow(()->new IllegalArgumentException("item not found"));

        item.setPage(null);
        itemRepository.save(item);

    }


}
