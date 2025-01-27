package jpabasic.inspacebe.service;

import jakarta.transaction.Transactional;
import jpabasic.inspacebe.dto.item.ArchiveRequestDto;
import jpabasic.inspacebe.dto.item.ArchiveRequestStickerDto;
import jpabasic.inspacebe.entity.*;
import jpabasic.inspacebe.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static jpabasic.inspacebe.dto.item.ArchiveRequestDto.toStickerRequestDto;

@Service
public class PageService {

    private final SpaceRepository spaceRepository;
    private final ItemRepository itemRepository;
    private final PageRepository pageRepository;
    private final UserRepository userRepository;
    private final StickerRepository stickerRepository;

    public PageService(SpaceRepository spaceRepository, ItemRepository itemRepository, PageRepository pageRepository, UserRepository userRepository, StickerRepository stickerRepository) {
        this.spaceRepository = spaceRepository;
        this.itemRepository = itemRepository;
        this.pageRepository = pageRepository;
        this.userRepository = userRepository;
        this.stickerRepository = stickerRepository;
    }

    //페이지 생성
    @Transactional
    public void createPages(Integer spaceId,User user){

        Space space=spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Space not found"));
//        User user=userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        for(int i=0;i<10;i++){
            Page page=new Page();
            page.setPageNumber(i+1);
            page.setSpace(space);
            page.setUser(user);
            pageRepository.save(page);
        }

    }


    // 페이지 조회
    @Transactional
    public List<ArchiveRequestDto> getPage(Integer spaceId, int pageNumber) {
        // Space 조회
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Space not found"));

        // Space에 포함된 Page 리스트 조회
        List<Page> pages = space.getPages();
        Page targetPage=null;

        for(Page page : pages){
            if(page.getPageNumber() == pageNumber){
                targetPage = page;
                break;
            }
        }
        Integer pageId= targetPage.getPageId();

        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new IllegalArgumentException("Page not found"));


        List<Item> items=page.getItems();
        if(items==null||items.isEmpty()){
            return Collections.emptyList();
        }else {
            // Item을 ArchiveRequestDto로 변환하여 반환
            return items.stream()
                    .map(ArchiveRequestDto::toArchiveDto)
                    .toList();
        }


    }


    //아이템 페이지(아카이브)에 등록 //pageNum 수정 필요.
    @Transactional
    public void archiveItems (Integer pageId,List<ArchiveRequestDto> dtoList) {

        Page page= pageRepository.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Page not found with id: " + pageId));


        for(ArchiveRequestDto dto : dtoList) {
            Item item= ArchiveRequestDto.toEntity(dto);
            item.setPage(page);
            itemRepository.save(item);

        }
    }

    //아카이브에 스티커 등록 전 stickerItem entity 생성
    @Transactional
    public List<ArchiveRequestStickerDto> postStickerItem (List<ArchiveRequestDto> dtoList) {

        List<ArchiveRequestStickerDto> stickers = new ArrayList<>();

        for(ArchiveRequestDto dto : dtoList) {
            if(dto.getCtype().equals(CType.STICKER)){
                String title=dto.getTitle();

                if(!stickerRepository.existsByTitle(title)) {

                    StickerItem stickerItem = new StickerItem();

                    stickerItem.setTitle(dto.getSticker().getTitle());
                    stickerItem.setSrc(dto.getSticker().getSrc());
                    stickerItem.setAlt(dto.getSticker().getAlt());
                    stickerItem.setColor(dto.getSticker().getColor());
                    stickerRepository.save(stickerItem);

                }


                ArchiveRequestStickerDto stickerDto=toStickerRequestDto(dto);
                stickers.add(stickerDto); // 리스트에 추가
            }
        }

        return stickers;
    }

    //아카이브에 스티커 등록 : Item entity 생성
    @Transactional
    public void archiveStickers(List<ArchiveRequestDto> dtoList,Integer pageId) {

        Page page=pageRepository.findById(pageId)
                .orElseThrow(()->new RuntimeException("Page not found with id: " + pageId));

        for(ArchiveRequestDto dto : dtoList) {

            if(dto.getCtype().equals(CType.STICKER)) {

//                StickerItem stickerItem = stickerRepository.findById(itemId)
//                        .orElseThrow(() -> new RuntimeException("sticker Item not found"));

                Item item = new Item();
                StickerItem stickerItem;


                item.setPage(page);
                item.setCtype(CType.STICKER);
                item.setPositionX(dto.getPositionX());
                item.setPositionY(dto.getPositionY());
                item.setHeight(dto.getHeight());
                item.setWidth(dto.getWidth());
                item.setTurnover(dto.getTurnover());
                item.setSequence(dto.getSequence());
                itemRepository.save(item);


                dto.setItemId(item.getItemId());
                String title=dto.getSticker().getTitle();

                if(!stickerRepository.existsByTitle(title)) {

                    stickerItem = new StickerItem();
                    stickerItem.setItemId(item.getItemId());
                    stickerItem.setTitle(dto.getSticker().getTitle());
                    stickerItem.setSrc(dto.getSticker().getSrc());
                    stickerItem.setAlt(dto.getSticker().getAlt());
                    stickerItem.setColor(dto.getSticker().getColor());
                    stickerRepository.save(stickerItem);

                }else{
                    stickerItem=stickerRepository.findByTitle(title);

                }


                item.setStickerItem(stickerItem);
                itemRepository.save(item);




//            ArchiveRequestStickerDto stickerDto=toStickerRequestDto(dto);
//            stickers.add(stickerDto); // 리스트에 추가
            }
            }
        }




    //페이지(아카이브)에서 아이템 삭제
    @Transactional
    public void deleteItemOnPage(String itemId) {
        Item item=itemRepository.findById(itemId)
                .orElseThrow(()->new IllegalArgumentException("item not found"));

        item.setPage(null);
        itemRepository.save(item);

    }





}
