package com.clonecoding.pinterest.domain.pin.repository;

import com.clonecoding.pinterest.domain.pin.entity.Pin;
import com.clonecoding.pinterest.domain.pin.entity.PinLike;
import com.clonecoding.pinterest.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PinLikeRepository extends JpaRepository<PinLike, Long>{
    Optional<PinLike> findByPinAndUser(Pin pin, User user);
}
