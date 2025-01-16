package jpabasic.inspacebe.service;

import jpabasic.inspacebe.dto.SpaceDto;
import jpabasic.inspacebe.entity.Space;
import jpabasic.inspacebe.entity.User;
import jpabasic.inspacebe.repository.SpaceRepository;
import jpabasic.inspacebe.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;

    public SpaceService(SpaceRepository spaceRepository, UserRepository userRepository) {
        this.spaceRepository = spaceRepository;
        this.userRepository = userRepository;
    }

    //새로운 공간 생성
    public SpaceDto createSpace(SpaceDto dto, Integer userId) {


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        //유저의 첫 공간 일 때, 해당 공간을 대표 공간으로 설정.\
        //유저가 직접 공간을 '대표공간'이라고 설정 -> 원래 있던 대표 공간을 false로 바꾸기
        List<Space> spaces = user.getSpaces();

        if (spaces.isEmpty()) {
            dto.setIsPublic(true);
        } else {
            for (Space target : spaces) {
                if (target.getIsPublic().equals(true)) {
                    target.setIsPublic(false);
                    System.out.println("다른거 false 처리하고 true처리함");
                    break;
                }
            }
            dto.setIsPublic(true);
        }


            Space space = Space.toEntity(dto);
            space.setUser(user);
            spaceRepository.save(space);

            return SpaceDto.toDto(space);

    }

    //공간 목록 조회
    public List<SpaceDto> getSpaces(Integer userId){
        //1. userId->userRepository에서 해당 유저의 공간들 리스트로 뽑는다
        User user=userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User Not Found"));
        List<Space> spaces=user.getSpaces();
        List<SpaceDto> spaceDtos=spaces.stream().map(SpaceDto::toDto).collect(Collectors.toList());

        return spaceDtos;
    }

    //특정 공간 조회 //타인이 조회 -> 조회할 수 없도록. //본인이 조회 -> 상관X
    public SpaceDto getSpace(Integer spaceId){
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