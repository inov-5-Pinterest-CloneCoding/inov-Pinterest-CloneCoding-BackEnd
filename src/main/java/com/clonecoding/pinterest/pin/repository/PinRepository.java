package com.clonecoding.pinterest.pin.repository;

import com.clonecoding.pinterest.pin.entity.Pin;
import com.clonecoding.pinterest.user.entity.User;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PinRepository extends JpaRepository<Pin, Long> {
    //조회하기
    Optional<Pin> findById(Long id);
    //
    List<Pin> findAllByPin(Pin pin);
    //삭제하기
    void deleteAllByPin(Pin pin);
    // 무한 스크롤
    Slice<User> findByLastname(String username);
}
