package com.clonecoding.pinterest.pin.service;

import com.clonecoding.pinterest.pin.dto.PinDTO;
import com.clonecoding.pinterest.pin.dto.PinRequestDTO;
import com.clonecoding.pinterest.pin.dto.PinResponseDTO;
import com.clonecoding.pinterest.pin.entity.Pin;
import com.clonecoding.pinterest.pin.exception.PinNotFoundException;
import com.clonecoding.pinterest.pin.repository.PinRepository;
import com.clonecoding.pinterest.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class PinService {

    @Autowired
    private PinRepository pinRepository;

    public PinResponseDTO createPin(PinRequestDTO requestDto, User user) throws IOException {
        Pin pin = new Pin(requestDto,user);
        pinRepository.save(pin);
        return null;
    }











    public Pin getPinById(Long id){
        return pinRepository.findById(id)
                .orElseThrow(() -> new PinNotFoundException("Pin을 찾을 수 없습니다."));
    }

}
