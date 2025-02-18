package jpabasic.inspacebe.service;

import jakarta.transaction.Transactional;
import jpabasic.inspacebe.dto.SpaceDto;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.entity.User;
import jpabasic.inspacebe.repository.SpaceRepository;
import jpabasic.inspacebe.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Transactional
@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;
    private final PageService pageService;

    public SpaceService(SpaceRepository spaceRepository, UserRepository userRepository, PageService pageService) {
        this.spaceRepository = spaceRepository;
        this.userRepository = userRepository;
        this.pageService = pageService;
    }

    //새로운 공간 생성
    @Transactional
    public SpaceDto createSpace(SpaceDto dto, User user) {

//        User user=userRepository.findById(userId).get();

        //유저의 첫 공간 일 때, 해당 공간을 대표 공간으로 설정.\
        //유저가 직접 공간을 '대표공간'이라고 설정 -> 원래 있던 대표 공간을 false로 바꾸기
        List<Space> spaces = user.getSpaces();


        if (spaces.isEmpty()) {
            dto.setIsPrimary(true);
        } else {
            if(dto.getIsPrimary()) {
                for (Space target : spaces) {
                    if (target.getIsPrimary().equals(true)) {
                        target.setIsPrimary(false);
                        spaceRepository.save(target);
                        System.out.println("다른거 false 처리하고 true처리함");
                        break;
                    }
                }
                dto.setIsPrimary(true);
            }
        }


        Space space = Space.toEntity(dto);
        space.setUser(user);
        spaceRepository.save(space);


        Integer pageId = space.getSpaceId();
        pageService.createPages(pageId, user);


        return SpaceDto.toDto(space);

    }

    //공간 목록 조회

    @Transactional
    public List<SpaceDto> getSpaces(User user) {
//        User user=userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        List<Space> spaces = user.getSpaces();
        if (spaces == null || spaces.isEmpty()) {
            throw new NoSuchElementException("사용자의 공간 목록이 비어 있습니다.");
        }
        return spaces.stream()
                .map(SpaceDto::toDto)
                .collect(Collectors.toList());
    }


    //특정 공간 조회 //타인이 조회 -> 조회할 수 없도록. //본인이 조회 -> 상관X

    @Transactional
    public SpaceDto getSpace(Integer spaceId) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공간입니다."));
        SpaceDto responseDto = SpaceDto.toDto(space);
        return responseDto;
    }


    @Transactional
    public ResponseEntity<SpaceDto> updateSpace(Integer spaceId, Map<String,Object> updates) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공간입니다."));
        System.out.println(updates.get("isPublic"));
        System.out.println(updates.get("isPrimary"));

        // isPublic 인 경우
        String key=updates.keySet().iterator().next();
        if("isPublic".equals(key)) {
            space.setIsPublic((Boolean) updates.get("isPublic"));

        }else {
            //대표공간은 1개만 존재할 수 있다는 로직
            User user = space.getUser();
            List<Space> spaceList = user.getSpaces();

            for (Space s : spaceList) {
                if (s.getIsPrimary().equals(true)) {
                    s.setIsPrimary(false);
                    spaceRepository.save(s);

                }
            }

            space.setIsPrimary(true);

        }
        spaceRepository.save(space);
        return ResponseEntity.ok(SpaceDto.toDto(space));
    }


    //공간 삭제

    @Transactional
    public void deleteSpace(Integer spaceId) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공간입니다."));

        try {
            spaceRepository.delete(space);
        } catch (Exception e) {
            throw new RuntimeException("삭제 하는 데 실패했습니다.");
        }

    }

    // space 와 연관된 pages 데이터 가져오기
    @Transactional
    public SpaceDto getSpaceWithPages(Integer spaceId) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공간입니다."));
        return SpaceDto.toDto(space);
    }
}