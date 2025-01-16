package jpabasic.inspacebe.service;

import jpabasic.inspacebe.dto.page.PageDto;
import jpabasic.inspacebe.entity.Page;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.repository.SpaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {

    private final SpaceRepository spaceRepository;

    public PageService(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    //페이지 생성
    public void createPages(Integer spaceId){

        Space space=spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Space not found"));

        for(int i=0;i<10;i++){
            Page page=new Page();
            page.setPageNumber(i+1);
            page.setSpace(space);
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


}
