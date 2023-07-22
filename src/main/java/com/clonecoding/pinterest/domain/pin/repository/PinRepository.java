package com.clonecoding.pinterest.domain.pin.repository;

import com.clonecoding.pinterest.domain.pin.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinRepository extends JpaRepository<Pin, Long> {
}
