package com.clonecoding.pinterest.domain.pin.service;

import com.clonecoding.pinterest.domain.pin.dto.BaseResponseDTO;
import com.clonecoding.pinterest.domain.pin.dto.PinRequestDTO;
import com.clonecoding.pinterest.domain.pin.dto.PinResponseDTO;
import com.clonecoding.pinterest.domain.pin.entity.Pin;
import com.clonecoding.pinterest.domain.pin.repository.PinRepository;
import com.clonecoding.pinterest.domain.user.entity.User;
import com.clonecoding.pinterest.domain.user.entity.UserRoleEnum;
import com.clonecoding.pinterest.domain.user.repository.UserRepository;
import com.clonecoding.pinterest.global.S3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PinService {


    private final PinRepository pinRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;


    //생성
    public PinResponseDTO createPin(Long id, PinRequestDTO requestDTO){
        User user = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없습니다.")
        );
        String imgFileUrl = null;

        MultipartFile imageFile = requestDTO.getImageFile();
        if (!imageFile.isEmpty()){
            try{
                imgFileUrl = s3Service.uploadFile(imageFile);
            }catch(Exception e){
                throw e;
            }
        }

        Pin pin = new Pin(imgFileUrl, user);
        Pin savePin = pinRepository.save(pin);

        PinResponseDTO responseDTO = new PinResponseDTO(savePin);

        return responseDTO;
    }

    //수정
    @Transactional
    public PinResponseDTO modifyPin(Long id, PinRequestDTO requestDTO, User user){

        Pin pin = pinRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 Pin 입니다.")
        );


        if(!user.getRole().equals(UserRoleEnum.ADMIN)){
            if(!(pin.getUser().getId().equals(user.getId()))){
                throw new IllegalArgumentException("허가 받지 않은 사용자 입니다.");
            }
        }

        String imgFileUrl = s3Service.uploadFile(requestDTO.getImageFile());

        MultipartFile imageFile = requestDTO.getImageFile();
        if(!imageFile.isEmpty()){
            try{
                imgFileUrl = s3Service.uploadFile(imageFile);
            }catch (Exception e){
                throw e;
            }
        }

        if(hasRole(user,pin)){
            pin.modifyPin(imgFileUrl);
        }else {
            throw new IllegalArgumentException("수정 하실 수 없습니다.");
        }

        PinResponseDTO responseDTO = new PinResponseDTO(pin);
        return responseDTO;


    }



    //삭제
    @Transactional
    public BaseResponseDTO deletePin(Long id, User user){
        Pin pin = pinRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 Pin입니다.")
        );

        if(hasRole(user, pin)){
            pinRepository.delete(pin);
            return new BaseResponseDTO("게시글 삭제 완료", 200);
        }else {
            throw new IllegalArgumentException("게시글 삭제 실패 ");
        }
    }






    //권한 확인
    private boolean hasRole(User user , Pin pin){
        if(!user.getRole().equals(UserRoleEnum.ADMIN)){
            if(!(pin.getUser().getId().equals(user.getId()))){
                return false;
            }
        }
        return true;
    }
}
