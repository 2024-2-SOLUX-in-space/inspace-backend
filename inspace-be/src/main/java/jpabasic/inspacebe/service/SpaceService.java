package jpabasic.inspacebe.service;

import jpabasic.inspacebe.dto.SpaceDto;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.repository.SpaceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;

    public SpaceService(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    //새로운 공간 생성 //대표 공간 설정 로직 추가해야
    public SpaceDto createSpace(SpaceDto dto, int userId){
        Space space=Space.toEntity(dto);
        //1. userId->userRepository에서 해당 유저의 공간들 리스트로 뽑는다
        //2. 리스트에 공간 자체가 없을 때
        //3. 대표 공간으로 설정


        //space.setUserId(userId);
        spaceRepository.save(space);

        return SpaceDto.toDto(space);
    }

    //공간 목록 조회
//    public List<SpaceDto> getSpaces(int userId){
//        //1. userId->userRepository에서 해당 유저의 공간들 리스트로 뽑는다
//
//    }

    //특정 공간 조회 //타인이 조회 -> 조회할 수 없도록. //본인이 조회 -> 상관X
    public SpaceDto getSpace(int spaceId){
            Space space=spaceRepository.findById(spaceId)
                    .orElseThrow(()->new IllegalArgumentException("존재하지 않는 공간입니다."));
        SpaceDto responseDto=SpaceDto.toDto(space);
        return responseDto;
    }

    //공간 설정 변경
    public ResponseEntity<SpaceDto> updateSpace(int spaceId, SpaceDto dto){
        Space space=spaceRepository.findById(spaceId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 공간입니다."));

        //코드 더러움..수정필요.
        if(dto.getSname()!=space.getSname()) space.setSname(dto.getSname());
        if(dto.getSthumb()!=space.getSthumb()){
            space.setSthumb(dto.getSthumb());
        }
        if(dto.getIsPublic()!=space.getIsPublic()) space.setIsPublic(dto.getIsPublic());
        if(dto.getIsPrimary()!=space.getIsPrimary()) space.setIsPrimary(dto.getIsPrimary());

        spaceRepository.save(space);
        dto=SpaceDto.toDto(space);
        return ResponseEntity.ok(dto);
    }
    
    //공간 삭제
    public void deleteSpace(int spaceId){

        Space space=spaceRepository.findById(spaceId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 공간입니다."));

        try{
            spaceRepository.delete(space);
        }catch (Exception e){
            throw new RuntimeException("삭제 하는 데 실패했습니다.");
        }
        
    }
}
